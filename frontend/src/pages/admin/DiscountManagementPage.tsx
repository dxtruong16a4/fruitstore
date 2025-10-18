import React, { useEffect, useState } from 'react';
import {
  Container,
  Card,
  CardContent,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  TablePagination,
  IconButton,
  Chip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { LocalOffer, Edit, Delete, Add, CheckCircle, Cancel } from '@mui/icons-material';
import { adminApi, type DiscountResponse } from '../../api/adminApi';

const DiscountManagementPage: React.FC = () => {
  const [discounts, setDiscounts] = useState<DiscountResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingDiscount, setEditingDiscount] = useState<DiscountResponse | null>(null);
  const [formData, setFormData] = useState({
    code: '',
    description: '',
    discountType: 'PERCENTAGE' as 'PERCENTAGE' | 'FIXED_AMOUNT',
    discountValue: 0,
    maxUsageCount: undefined as number | undefined,
    expiryDate: '',
  });
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<number | null>(null);

  useEffect(() => {
    fetchDiscounts();
  }, [page, rowsPerPage]);

  const fetchDiscounts = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching discounts from page:', page, 'size:', rowsPerPage);
      const result = await adminApi.getAllDiscounts(page, rowsPerPage);
      console.log('Discounts API response:', result);

      if (result.success && result.data) {
        console.log('Discounts data:', result.data);
        setDiscounts(result.data.content || []);
        setTotalElements(result.data.totalElements || 0);
      } else {
        const errorMsg = result.message || 'Failed to load discounts';
        console.error('API error:', errorMsg);
        setError(errorMsg);
      }
    } catch (err) {
      const errorMsg = 'An error occurred while loading discounts';
      console.error(errorMsg, err);
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleChangePage = (_event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleOpenDialog = (discount?: DiscountResponse) => {
    if (discount) {
      setEditingDiscount(discount);
      setFormData({
        code: discount.code,
        description: discount.description || '',
        discountType: discount.discountType,
        discountValue: discount.discountValue,
        maxUsageCount: discount.maxUsageCount,
        expiryDate: discount.expiryDate ? discount.expiryDate.split('T')[0] : '',
      });
    } else {
      setEditingDiscount(null);
      setFormData({
        code: '',
        description: '',
        discountType: 'PERCENTAGE',
        discountValue: 0,
        maxUsageCount: undefined,
        expiryDate: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingDiscount(null);
  };

  const handleSave = async () => {
    if (!formData.code.trim() || formData.discountValue <= 0) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      setSaving(true);
      let result;

      if (editingDiscount) {
        result = await adminApi.updateDiscount(editingDiscount.discountId, formData);
      } else {
        result = await adminApi.createDiscount(formData);
      }

      if (result.success) {
        await fetchDiscounts();
        handleCloseDialog();
      } else {
        setError(result.message || 'Failed to save discount');
      }
    } catch (err) {
      setError('An error occurred while saving discount');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleToggleStatus = async (discount: DiscountResponse) => {
    try {
      setSaving(true);
      const result = discount.isActive
        ? await adminApi.deactivateDiscount(discount.discountId)
        : await adminApi.activateDiscount(discount.discountId);

      if (result.success) {
        await fetchDiscounts();
      } else {
        setError(result.message || 'Failed to update discount status');
      }
    } catch (err) {
      setError('An error occurred while updating discount status');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (discountId: number) => {
    try {
      setSaving(true);
      const result = await adminApi.deleteDiscount(discountId);

      if (result.success) {
        await fetchDiscounts();
        setDeleteConfirm(null);
      } else {
        setError(result.message || 'Failed to delete discount');
      }
    } catch (err) {
      setError('An error occurred while deleting discount');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  if (loading && discounts.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1, display: 'flex', alignItems: 'center', gap: 1 }}>
            <LocalOffer sx={{ fontSize: 32 }} />
            Discount Management
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Manage promotional discounts and coupon codes
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => handleOpenDialog()}
        >
          Add Discount
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
          {error}
        </Alert>
      )}

      <Card>
        <CardContent>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                  <TableCell sx={{ fontWeight: 'bold' }}>Code</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Type</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="right">Value</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="center">Usage</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Status</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {discounts.map((discount) => (
                  <TableRow key={discount.discountId} hover>
                    <TableCell sx={{ fontWeight: 500 }}>{discount.code}</TableCell>
                    <TableCell>
                      <Chip
                        label={discount.discountType === 'PERCENTAGE' ? '%' : '₫'}
                        size="small"
                        variant="outlined"
                      />
                    </TableCell>
                    <TableCell align="right">
                      {discount.discountType === 'PERCENTAGE'
                        ? `${discount.discountValue}%`
                        : `₫${discount.discountValue.toLocaleString()}`}
                    </TableCell>
                    <TableCell align="center">
                      {discount.usageCount}
                      {discount.maxUsageCount && ` / ${discount.maxUsageCount}`}
                    </TableCell>
                    <TableCell>
                      <Chip
                        icon={discount.isActive ? <CheckCircle /> : <Cancel />}
                        label={discount.isActive ? 'Active' : 'Inactive'}
                        color={discount.isActive ? 'success' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <IconButton
                        size="small"
                        onClick={() => handleOpenDialog(discount)}
                        title="Edit"
                      >
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleToggleStatus(discount)}
                        disabled={saving}
                        title={discount.isActive ? 'Deactivate' : 'Activate'}
                      >
                        {discount.isActive ? <Cancel fontSize="small" /> : <CheckCircle fontSize="small" />}
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => setDeleteConfirm(discount.discountId)}
                        disabled={saving}
                        title="Delete"
                      >
                        <Delete fontSize="small" color="error" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25, 50]}
            component="div"
            count={totalElements}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </CardContent>
      </Card>

      {/* Create/Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingDiscount ? 'Edit Discount' : 'Add New Discount'}
        </DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Discount Code"
              fullWidth
              value={formData.code}
              onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
              placeholder="e.g., SUMMER20"
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={2}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Enter discount description"
            />
            <FormControl fullWidth>
              <InputLabel>Discount Type</InputLabel>
              <Select
                value={formData.discountType}
                label="Discount Type"
                onChange={(e) => setFormData({ ...formData, discountType: e.target.value as 'PERCENTAGE' | 'FIXED_AMOUNT' })}
              >
                <MenuItem value="PERCENTAGE">Percentage (%)</MenuItem>
                <MenuItem value="FIXED_AMOUNT">Fixed Amount (₫)</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Discount Value"
              fullWidth
              type="number"
              value={formData.discountValue}
              onChange={(e) => setFormData({ ...formData, discountValue: parseFloat(e.target.value) })}
              placeholder="Enter discount value"
            />
            <TextField
              label="Max Usage Count (Optional)"
              fullWidth
              type="number"
              value={formData.maxUsageCount || ''}
              onChange={(e) => setFormData({ ...formData, maxUsageCount: e.target.value ? parseInt(e.target.value) : undefined })}
              placeholder="Leave empty for unlimited"
            />
            <TextField
              label="Expiry Date (Optional)"
              fullWidth
              type="date"
              value={formData.expiryDate}
              onChange={(e) => setFormData({ ...formData, expiryDate: e.target.value })}
              InputLabelProps={{ shrink: true }}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button 
            onClick={handleSave} 
            variant="contained" 
            disabled={saving || !formData.code.trim() || formData.discountValue <= 0}
          >
            {saving ? 'Saving...' : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog open={deleteConfirm !== null} onClose={() => setDeleteConfirm(null)}>
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this discount? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirm(null)}>Cancel</Button>
          <Button 
            onClick={() => deleteConfirm !== null && handleDelete(deleteConfirm)}
            variant="contained"
            color="error"
            disabled={saving}
          >
            {saving ? 'Deleting..' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default DiscountManagementPage;

