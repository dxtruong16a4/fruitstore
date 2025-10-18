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
  Chip,
} from '@mui/material';
import {
  ShoppingCart,
  People,
  Inventory,
  LocalOffer,
  TrendingUp,
  CheckCircle,
  Schedule,
  LocalShipping,
  Cancel,
} from '@mui/icons-material';
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import { adminApi, type DashboardStatistics, type RecentOrder } from '../../api/adminApi';

interface StatCard {
  title: string;
  value: number;
  icon: React.ReactNode;
  color: string;
  subtext?: string;
}

const DashboardPage: React.FC = () => {
  const [statistics, setStatistics] = useState<DashboardStatistics | null>(null);
  const [recentOrders, setRecentOrders] = useState<RecentOrder[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        setError(null);

        const [statsResult, ordersResult] = await Promise.all([
          adminApi.getDashboardStatistics(),
          adminApi.getRecentOrders(7, 10),
        ]);

        if (statsResult.success && statsResult.data) {
          setStatistics(statsResult.data);
        } else {
          setError(statsResult.message || 'Failed to load statistics');
        }

        if (ordersResult.success && ordersResult.data) {
          setRecentOrders(ordersResult.data);
        }
      } catch (err) {
        setError('An error occurred while loading dashboard data');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const getStatusColor = (status: string): 'default' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' => {
    switch (status?.toUpperCase()) {
      case 'PENDING':
        return 'warning';
      case 'CONFIRMED':
        return 'info';
      case 'SHIPPED':
        return 'primary';
      case 'DELIVERED':
        return 'success';
      case 'CANCELLED':
        return 'error';
      default:
        return 'default';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status?.toUpperCase()) {
      case 'PENDING':
        return <Schedule sx={{ fontSize: 16 }} />;
      case 'CONFIRMED':
        return <CheckCircle sx={{ fontSize: 16 }} />;
      case 'SHIPPED':
        return <LocalShipping sx={{ fontSize: 16 }} />;
      case 'DELIVERED':
        return <CheckCircle sx={{ fontSize: 16 }} />;
      case 'CANCELLED':
        return <Cancel sx={{ fontSize: 16 }} />;
      default:
        return undefined;
    }
  };

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <CircularProgress />
      </Container>
    );
  }

  const statCards: StatCard[] = statistics ? [
    {
      title: 'Total Orders',
      value: statistics.totalOrders,
      icon: <ShoppingCart sx={{ fontSize: 32 }} />,
      color: '#1976d2',
      subtext: `Revenue: ₫${(statistics.totalRevenue || 0).toLocaleString()}`,
    },
    {
      title: 'Pending Orders',
      value: statistics.pendingOrders,
      icon: <Schedule sx={{ fontSize: 32 }} />,
      color: '#ff9800',
    },
    {
      title: 'Delivered Orders',
      value: statistics.deliveredOrders,
      icon: <CheckCircle sx={{ fontSize: 32 }} />,
      color: '#4caf50',
    },
    {
      title: 'Total Products',
      value: statistics.totalProducts,
      icon: <Inventory sx={{ fontSize: 32 }} />,
      color: '#9c27b0',
      subtext: `Low stock: ${statistics.lowStockProducts}`,
    },
    {
      title: 'Categories',
      value: statistics.totalCategories,
      icon: <LocalOffer sx={{ fontSize: 32 }} />,
      color: '#f44336',
      subtext: `Active: ${statistics.activeCategories}`,
    },
    {
      title: 'Active Discounts',
      value: statistics.activeDiscounts,
      icon: <TrendingUp sx={{ fontSize: 32 }} />,
      color: '#00bcd4',
      subtext: `Total: ${statistics.totalDiscounts}`,
    },
    {
      title: 'Total Users',
      value: statistics.totalUsers,
      icon: <People sx={{ fontSize: 32 }} />,
      color: '#673ab7',
      subtext: `Admins: ${statistics.adminUsers}`,
    },
    {
      title: 'Avg Order Value',
      value: statistics.averageOrderValue,
      icon: <TrendingUp sx={{ fontSize: 32 }} />,
      color: '#2196f3',
      subtext: '₫' + (statistics.averageOrderValue || 0).toLocaleString(),
    },
  ] : [];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          📊 Admin Dashboard
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Welcome to the FruitStore admin dashboard. Monitor your business metrics and manage operations.
        </Typography>
      </Box>

      {/* Error Alert */}
      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}

      {/* Statistics Cards Grid */}
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2, mb: 4 }}>
        {statCards.map((card, index) => (
          <Box key={index}>
            <Card
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                transition: 'transform 0.2s, box-shadow 0.2s',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 3,
                },
              }}
            >
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                  <Typography color="textSecondary" gutterBottom>
                    {card.title}
                  </Typography>
                  <Box sx={{ color: card.color }}>
                    {card.icon}
                  </Box>
                </Box>
                <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 1 }}>
                  {card.value.toLocaleString()}
                </Typography>
                {card.subtext && (
                  <Typography variant="caption" color="textSecondary">
                    {card.subtext}
                  </Typography>
                )}
              </CardContent>
            </Card>
          </Box>
        ))}
      </Box>

      {/* Charts Section */}
      {statistics && (
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' }, gap: 3, mb: 4 }}>
          {/* Order Status Chart */}
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
                📈 Order Status Distribution
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={[
                      { name: 'Pending', value: statistics.pendingOrders, fill: '#ff9800' },
                      { name: 'Confirmed', value: statistics.confirmedOrders, fill: '#2196f3' },
                      { name: 'Shipped', value: statistics.shippedOrders, fill: '#9c27b0' },
                      { name: 'Delivered', value: statistics.deliveredOrders, fill: '#4caf50' },
                      { name: 'Cancelled', value: statistics.cancelledOrders, fill: '#f44336' },
                    ]}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, value }) => `${name}: ${value}`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    <Cell fill="#ff9800" />
                    <Cell fill="#2196f3" />
                    <Cell fill="#9c27b0" />
                    <Cell fill="#4caf50" />
                    <Cell fill="#f44336" />
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          {/* Revenue Chart */}
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
                💰 Revenue Overview
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart
                  data={[
                    { name: 'Total Revenue', value: statistics.totalRevenue },
                    { name: 'Avg Order Value', value: statistics.averageOrderValue },
                  ]}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip formatter={(value) => `₫${(value as number).toLocaleString()}`} />
                  <Bar dataKey="value" fill="#2196f3" />
                </BarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </Box>
      )}

      {/* Recent Orders Section */}
      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
            📋 Recent Orders (Last 7 Days)
          </Typography>
          {recentOrders.length > 0 ? (
            <TableContainer component={Paper} variant="outlined">
              <Table size="small">
                <TableHead>
                  <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell>Order #</TableCell>
                    <TableCell>Customer</TableCell>
                    <TableCell align="right">Amount</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Date</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {recentOrders.map((order) => (
                    <TableRow key={order.orderId} hover>
                      <TableCell sx={{ fontWeight: 'bold' }}>
                        {order.orderNumber}
                      </TableCell>
                      <TableCell>{order.customerName}</TableCell>
                      <TableCell align="right">
                        ₫{order.totalAmount.toLocaleString()}
                      </TableCell>
                      <TableCell>
                        <Chip
                          icon={getStatusIcon(order.status)}
                          label={order.status}
                          color={getStatusColor(order.status)}
                          size="small"
                          variant="outlined"
                        />
                      </TableCell>
                      <TableCell>
                        {new Date(order.createdAt).toLocaleDateString('vi-VN')}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          ) : (
            <Typography color="textSecondary">No recent orders found</Typography>
          )}
        </CardContent>
      </Card>

      {/* Order Status Summary */}
      {statistics && (
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2 }}>
          <Box>
            <Card sx={{ backgroundColor: '#fff3e0' }}>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Pending
                </Typography>
                <Typography variant="h5" sx={{ color: '#ff9800', fontWeight: 'bold' }}>
                  {statistics.pendingOrders}
                </Typography>
              </CardContent>
            </Card>
          </Box>
          <Box>
            <Card sx={{ backgroundColor: '#e3f2fd' }}>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Confirmed
                </Typography>
                <Typography variant="h5" sx={{ color: '#2196f3', fontWeight: 'bold' }}>
                  {statistics.confirmedOrders}
                </Typography>
              </CardContent>
            </Card>
          </Box>
          <Box>
            <Card sx={{ backgroundColor: '#f3e5f5' }}>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Shipped
                </Typography>
                <Typography variant="h5" sx={{ color: '#9c27b0', fontWeight: 'bold' }}>
                  {statistics.shippedOrders}
                </Typography>
              </CardContent>
            </Card>
          </Box>
          <Box>
            <Card sx={{ backgroundColor: '#e8f5e9' }}>
              <CardContent>
                <Typography color="textSecondary" gutterBottom>
                  Delivered
                </Typography>
                <Typography variant="h5" sx={{ color: '#4caf50', fontWeight: 'bold' }}>
                  {statistics.deliveredOrders}
                </Typography>
              </CardContent>
            </Card>
          </Box>
        </Box>
      )}
    </Container>
  );
};

export default DashboardPage;

