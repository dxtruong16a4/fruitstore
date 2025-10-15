import React from 'react';
import {
  Box,
  Typography,
  Container,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  Paper,
} from '@mui/material';
import {
  ShoppingCart,
  LocalShipping,
  Support,
  Security,
} from '@mui/icons-material';

const HomePage: React.FC = () => {
  const features = [
    {
      icon: <ShoppingCart sx={{ fontSize: 40, color: '#2e7d32' }} />,
      title: 'Mua sắm dễ dàng',
      description: 'Duyệt và mua sắm trái cây tươi ngon một cách thuận tiện',
    },
    {
      icon: <LocalShipping sx={{ fontSize: 40, color: '#2e7d32' }} />,
      title: 'Giao hàng nhanh',
      description: 'Giao hàng tận nơi trong thời gian ngắn nhất',
    },
    {
      icon: <Support sx={{ fontSize: 40, color: '#2e7d32' }} />,
      title: 'Hỗ trợ 24/7',
      description: 'Đội ngũ hỗ trợ khách hàng luôn sẵn sàng giúp đỡ',
    },
    {
      icon: <Security sx={{ fontSize: 40, color: '#2e7d32' }} />,
      title: 'Bảo mật thông tin',
      description: 'Thông tin cá nhân và thanh toán được bảo mật tuyệt đối',
    },
  ];

  return (
    <Container maxWidth="lg">
      {/* Hero Section */}
      <Paper
        elevation={3}
        sx={{
          background: 'linear-gradient(135deg, #4caf50 0%, #2e7d32 100%)',
          color: 'white',
          p: 6,
          mb: 4,
          borderRadius: 2,
          textAlign: 'center',
        }}
      >
        <Typography variant="h2" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          🍎 FruitStore
        </Typography>
        <Typography variant="h5" component="h2" gutterBottom sx={{ mb: 3 }}>
          Chào mừng đến với cửa hàng trái cây tươi ngon
        </Typography>
        <Typography variant="body1" sx={{ fontSize: '1.1rem', mb: 4, maxWidth: 600, mx: 'auto' }}>
          Khám phá thế giới trái cây tươi ngon với chất lượng cao và giá cả hợp lý. 
          Chúng tôi cam kết mang đến cho bạn những sản phẩm tốt nhất.
        </Typography>
        <Button
          variant="contained"
          size="large"
          sx={{
            backgroundColor: 'white',
            color: '#2e7d32',
            fontWeight: 'bold',
            px: 4,
            py: 1.5,
            '&:hover': {
              backgroundColor: '#f5f5f5',
            },
          }}
        >
          Khám phá sản phẩm
        </Button>
      </Paper>

      {/* Features Section */}
      <Box sx={{ mb: 6 }}>
        <Typography
          variant="h3"
          component="h2"
          gutterBottom
          sx={{ textAlign: 'center', mb: 4, fontWeight: 'bold', color: '#2e7d32' }}
        >
          Tại sao chọn FruitStore?
        </Typography>
        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  textAlign: 'center',
                  p: 2,
                  transition: 'transform 0.2s',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: 4,
                  },
                }}
              >
                <CardContent sx={{ flexGrow: 1 }}>
                  <Box sx={{ mb: 2 }}>
                    {feature.icon}
                  </Box>
                  <Typography variant="h6" component="h3" gutterBottom sx={{ fontWeight: 'bold' }}>
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Call to Action */}
      <Paper
        elevation={2}
        sx={{
          p: 4,
          textAlign: 'center',
          backgroundColor: '#f8f9fa',
          borderRadius: 2,
        }}
      >
        <Typography variant="h4" component="h2" gutterBottom sx={{ fontWeight: 'bold', color: '#2e7d32' }}>
          Bắt đầu mua sắm ngay hôm nay!
        </Typography>
        <Typography variant="body1" sx={{ mb: 3, color: 'text.secondary' }}>
          Khám phá bộ sưu tập trái cây tươi ngon của chúng tôi
        </Typography>
        <Button
          variant="contained"
          size="large"
          sx={{
            backgroundColor: '#2e7d32',
            fontWeight: 'bold',
            px: 4,
            py: 1.5,
            '&:hover': {
              backgroundColor: '#1b5e20',
            },
          }}
        >
          Xem tất cả sản phẩm
        </Button>
      </Paper>
    </Container>
  );
};

export default HomePage;
