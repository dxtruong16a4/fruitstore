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
  Chip,
  TablePagination,
  IconButton,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { ShoppingCart, Edit, Visibility } from '@mui/icons-material';
import { adminApi, type OrderResponse } from '../../api/adminApi';

const ORDER_STATUSES = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

const getStatusColor = (status: string): 'default' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' => {
  switch (status) {
    case 'PENDING': return 'warning';
    case 'CONFIRMED': return 'info';
    case 'SHIPPED': return 'primary';
    case 'DELIVERED': return 'success';
    case 'CANCELLED': return 'error';
    default: return 'default';
  }
};

const OrderManagementPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedOrder, setSelectedOrder] = useState<OrderResponse | null>(null);
  const [openViewDialog, setOpenViewDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [newStatus, setNewStatus] = useState('');
  const [notes, setNotes] = useState('');
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    fetchOrders();
  }, [page, rowsPerPage]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching orders from page:', page, 'size:', rowsPerPage);
      const result = await adminApi.getAllOrdersAdmin(page, rowsPerPage);
      console.log('Orders API response:', result);

      if (result.success && result.data) {
        console.log('Orders data:', result.data);
        // Orders API returns data.data (array) instead of data.content
        const ordersArray = result.data.data || result.data.content || [];
        setOrders(ordersArray);
        setTotalElements(result.data.totalElements || 0);
      } else {
        const errorMsg = result.message || 'Failed to load orders';
        console.error('API error:', errorMsg);
        setError(errorMsg);
      }
    } catch (err) {
      const errorMsg = 'An error occurred while loading orders';
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

  const handleViewOrder = (order: OrderResponse) => {
    setSelectedOrder(order);
    setOpenViewDialog(true);
  };

  const handleEditStatus = (order: OrderResponse) => {
    setSelectedOrder(order);
    setNewStatus(order.status);
    setNotes(order.notes || '');
    setOpenEditDialog(true);
  };

  const handleCloseViewDialog = () => {
    setOpenViewDialog(false);
    setSelectedOrder(null);
  };

  const handleCloseEditDialog = () => {
    setOpenEditDialog(false);
    setSelectedOrder(null);
    setNewStatus('');
    setNotes('');
  };

  const handleUpdateStatus = async () => {
    if (!selectedOrder) return;

    try {
      setUpdating(true);
      const result = await adminApi.updateOrderStatusAdmin(selectedOrder.orderId, newStatus, notes);
      
      if (result.success) {
        await fetchOrders();
        handleCloseEditDialog();
      } else {
        setError(result.message || 'Failed to update order status');
      }
    } catch (err) {
      setError('An error occurred while updating order status');
      console.error(err);
    } finally {
      setUpdating(false);
    }
  };

  if (loading && orders.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
          <ShoppingCart sx={{ fontSize: 32 }} />
          Order Management
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Manage customer orders and track shipments
        </Typography>
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
                  <TableCell sx={{ fontWeight: 'bold' }}>Order ID</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Customer</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="right">Total Amount</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Status</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Created At</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orders.map((order) => (
                  <TableRow key={order.orderId} hover>
                    <TableCell sx={{ fontWeight: 500 }}>#{order.orderId}</TableCell>
                    <TableCell>{order.username}</TableCell>
                    <TableCell align="right">₫{order.totalAmount.toLocaleString()}</TableCell>
                    <TableCell>
                      <Chip
                        label={order.status}
                        color={getStatusColor(order.status)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{new Date(order.createdAt).toLocaleDateString()}</TableCell>
                    <TableCell align="center">
                      <IconButton
                        size="small"
                        onClick={() => handleViewOrder(order)}
                        title="View Details"
                      >
                        <Visibility fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleEditStatus(order)}
                        title="Update Status"
                      >
                        <Edit fontSize="small" />
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

      {/* View Order Details Dialog */}
      <Dialog open={openViewDialog} onClose={handleCloseViewDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Order Details</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          {selectedOrder && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Box>
                <Typography variant="body2" color="text.secondary">Order ID</Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>#{selectedOrder.orderId}</Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">Customer</Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>{selectedOrder.username}</Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">Total Amount</Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>₫{selectedOrder.totalAmount.toLocaleString()}</Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">Status</Typography>
                <Chip label={selectedOrder.status} color={getStatusColor(selectedOrder.status)} size="small" />
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">Shipping Address</Typography>
                <Typography variant="body1">{selectedOrder.shippingAddress || '-'}</Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">Notes</Typography>
                <Typography variant="body1">{selectedOrder.notes || '-'}</Typography>
              </Box>
              {selectedOrder.orderItems && selectedOrder.orderItems.length > 0 && (
                <Box>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>Items</Typography>
                  {selectedOrder.orderItems.map((item) => (
                    <Box key={item.orderItemId} sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography variant="body2">{item.productName} x{item.quantity}</Typography>
                      <Typography variant="body2">₫{item.subtotal.toLocaleString()}</Typography>
                    </Box>
                  ))}
                </Box>
              )}
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseViewDialog}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Edit Status Dialog */}
      <Dialog open={openEditDialog} onClose={handleCloseEditDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Update Order Status</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          {selectedOrder && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Order: <strong>#{selectedOrder.orderId}</strong>
              </Typography>
              <FormControl fullWidth>
                <InputLabel>Status</InputLabel>
                <Select
                  value={newStatus}
                  label="Status"
                  onChange={(e) => setNewStatus(e.target.value)}
                >
                  {ORDER_STATUSES.map((status) => (
                    <MenuItem key={status} value={status}>
                      {status}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="Notes"
                fullWidth
                multiline
                rows={3}
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="Add notes about this order"
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseEditDialog}>Cancel</Button>
          <Button 
            onClick={handleUpdateStatus} 
            variant="contained" 
            disabled={updating || newStatus === selectedOrder?.status}
          >
            {updating ? 'Updating...' : 'Update'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default OrderManagementPage;

