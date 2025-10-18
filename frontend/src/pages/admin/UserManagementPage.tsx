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
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  TablePagination,
  Chip,
} from '@mui/material';
import { People, Edit } from '@mui/icons-material';
import { adminApi, type UserResponse } from '../../api/adminApi';

const UserManagementPage: React.FC = () => {
  const [users, setUsers] = useState<UserResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedUser, setSelectedUser] = useState<UserResponse | null>(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [newRole, setNewRole] = useState<'ADMIN' | 'CUSTOMER'>('CUSTOMER');
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, [page, rowsPerPage]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching users from page:', page, 'size:', rowsPerPage);
      const result = await adminApi.getAllUsers(page, rowsPerPage);
      console.log('Users API response:', result);

      if (result.success && result.data) {
        const pageData = result.data;
        console.log('Page data:', pageData);
        setUsers(pageData.content || []);
        setTotalElements(pageData.totalElements || 0);
      } else {
        const errorMsg = result.message || 'Failed to load users';
        console.error('API error:', errorMsg);
        setError(errorMsg);
      }
    } catch (err) {
      const errorMsg = 'An error occurred while loading users';
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

  const handleOpenDialog = (user: UserResponse) => {
    setSelectedUser(user);
    setNewRole(user.role);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedUser(null);
  };

  const handleUpdateRole = async () => {
    if (!selectedUser) return;

    try {
      setUpdating(true);
      const result = await adminApi.updateUserRole(selectedUser.userId, newRole);
      
      if (result.success) {
        // Update the user in the list
        setUsers(users.map(u => 
          u.userId === selectedUser.userId 
            ? { ...u, role: newRole }
            : u
        ));
        handleCloseDialog();
      } else {
        setError(result.message || 'Failed to update user role');
      }
    } catch (err) {
      setError('An error occurred while updating user role');
      console.error(err);
    } finally {
      setUpdating(false);
    }
  };

  const getRoleColor = (role: string): 'default' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' => {
    return role === 'ADMIN' ? 'error' : 'info';
  };

  if (loading && users.length === 0) {
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
          <People sx={{ fontSize: 32 }} />
          User Management
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Manage user accounts and roles
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
                  <TableCell sx={{ fontWeight: 'bold' }}>Username</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Email</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Full Name</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Role</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Created At</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.userId} hover>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.fullName}</TableCell>
                    <TableCell>
                      <Chip
                        label={user.role}
                        color={getRoleColor(user.role)}
                        size="small"
                        variant="outlined"
                      />
                    </TableCell>
                    <TableCell>{new Date(user.createdAt).toLocaleDateString()}</TableCell>
                    <TableCell align="center">
                      <Button
                        startIcon={<Edit />}
                        size="small"
                        variant="outlined"
                        onClick={() => handleOpenDialog(user)}
                      >
                        Edit Role
                      </Button>
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

      {/* Edit Role Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Update User Role</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          {selectedUser && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Typography variant="body2" color="text.secondary">
                User: <strong>{selectedUser.username}</strong> ({selectedUser.email})
              </Typography>
              <FormControl fullWidth>
                <InputLabel>Role</InputLabel>
                <Select
                  value={newRole}
                  label="Role"
                  onChange={(e) => setNewRole(e.target.value as 'ADMIN' | 'CUSTOMER')}
                >
                  <MenuItem value="CUSTOMER">Customer</MenuItem>
                  <MenuItem value="ADMIN">Admin</MenuItem>
                </Select>
              </FormControl>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button 
            onClick={handleUpdateRole} 
            variant="contained" 
            disabled={updating || newRole === selectedUser?.role}
          >
            {updating ? 'Updating...' : 'Update'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default UserManagementPage;

