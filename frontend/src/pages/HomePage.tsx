import React from 'react';
import {
  Box,
  Typography,
  Container,
  Grid,
  Card,
  CardContent,
  Button,
  Paper,
  Chip,
  Avatar,
} from '@mui/material';
import {
  ShoppingCart,
  LocalShipping,
  Support,
  Security,
  Star,
  Verified,
  Nature,
} from '@mui/icons-material';

const HomePage: React.FC = () => {
  const features = [
    {
      icon: <ShoppingCart sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Mua sắm dễ dàng',
      description: 'Duyệt và mua sắm trái cây tươi ngon một cách thuận tiện với giao diện thân thiện',
    },
    {
      icon: <LocalShipping sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Giao hàng nhanh',
      description: 'Giao hàng tận nơi trong 24h với đội ngũ vận chuyển chuyên nghiệp',
    },
    {
      icon: <Support sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Hỗ trợ 24/7',
      description: 'Đội ngũ hỗ trợ khách hàng luôn sẵn sàng giúp đỡ mọi lúc, mọi nơi',
    },
    {
      icon: <Security sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Bảo mật thông tin',
      description: 'Thông tin cá nhân và thanh toán được bảo mật tuyệt đối với công nghệ SSL',
    },
    {
      icon: <Nature sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Sản phẩm hữu cơ',
      description: '100% trái cây hữu cơ, không hóa chất, đảm bảo sức khỏe cho gia đình',
    },
    {
      icon: <Verified sx={{ fontSize: 50, color: '#2e7d32' }} />,
      title: 'Chất lượng đảm bảo',
      description: 'Cam kết chất lượng sản phẩm với chứng nhận an toàn thực phẩm',
    },
  ];

  const stats = [
    { number: '10,000+', label: 'Khách hàng hài lòng' },
    { number: '50,000+', label: 'Đơn hàng đã giao' },
    { number: '99.9%', label: 'Tỷ lệ hài lòng' },
    { number: '24/7', label: 'Hỗ trợ khách hàng' },
  ];

  const testimonials = [
    {
      name: 'Nguyễn Thị Mai',
      location: 'Hà Nội',
      rating: 5,
      comment: 'Trái cây rất tươi ngon, giao hàng nhanh. Tôi rất hài lòng với dịch vụ của FruitStore.',
      avatar: 'M',
    },
    {
      name: 'Trần Văn Nam',
      location: 'TP.HCM',
      rating: 5,
      comment: 'Chất lượng vượt ngoài mong đợi. Nhân viên tư vấn rất nhiệt tình và chuyên nghiệp.',
      avatar: 'N',
    },
    {
      name: 'Lê Thị Hoa',
      location: 'Đà Nẵng',
      rating: 5,
      comment: 'Giá cả hợp lý, sản phẩm chất lượng cao. Sẽ tiếp tục ủng hộ cửa hàng.',
      avatar: 'H',
    },
  ];

  return (
    <Box sx={{ minHeight: '100vh', width: '100%' }}>
      <Container maxWidth={false} sx={{ width: '100%', px: { xs: 2, sm: 3, md: 4 } }}>
        {/* Hero Section */}
        <Paper
          elevation={3}
          sx={{
            background: 'linear-gradient(135deg, #4caf50 0%, #2e7d32 100%)',
            color: 'white',
            p: { xs: 4, md: 8 },
            mb: 6,
            borderRadius: 3,
            textAlign: 'center',
            minHeight: '60vh',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
          }}
        >
          <Typography 
            variant="h1" 
            component="h1" 
            gutterBottom 
            sx={{ 
              fontWeight: 'bold',
              fontSize: { xs: '2.5rem', md: '4rem' },
              mb: 2
            }}
          >
            🍎 FruitStore
          </Typography>
          <Typography 
            variant="h4" 
            component="h2" 
            gutterBottom 
            sx={{ 
              mb: 4,
              fontSize: { xs: '1.5rem', md: '2rem' },
              fontWeight: 300
            }}
          >
            Chào mừng đến với cửa hàng trái cây tươi ngon
          </Typography>
          <Typography 
            variant="h6" 
            sx={{ 
              mb: 6, 
              maxWidth: 800, 
              mx: 'auto',
              lineHeight: 1.8,
              fontSize: { xs: '1rem', md: '1.25rem' }
            }}
          >
            Khám phá thế giới trái cây tươi ngon với chất lượng cao và giá cả hợp lý. 
            Chúng tôi cam kết mang đến cho bạn những sản phẩm tốt nhất từ các trang trại hữu cơ hàng đầu Việt Nam.
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              size="large"
              sx={{
                backgroundColor: 'white',
                color: '#2e7d32',
                fontWeight: 'bold',
                px: 6,
                py: 2,
                fontSize: '1.1rem',
                '&:hover': {
                  backgroundColor: '#f5f5f5',
                  transform: 'translateY(-2px)',
                },
                transition: 'all 0.3s',
              }}
            >
              Khám phá sản phẩm
            </Button>
            <Button
              variant="outlined"
              size="large"
              sx={{
                borderColor: 'white',
                color: 'white',
                fontWeight: 'bold',
                px: 6,
                py: 2,
                fontSize: '1.1rem',
                '&:hover': {
                  backgroundColor: 'rgba(255,255,255,0.1)',
                  borderColor: 'white',
                  transform: 'translateY(-2px)',
                },
                transition: 'all 0.3s',
              }}
            >
              Tìm hiểu thêm
            </Button>
          </Box>
        </Paper>

        {/* Stats Section */}
        <Paper
          elevation={2}
          sx={{
            p: 4,
            mb: 6,
            borderRadius: 3,
            backgroundColor: '#f8f9fa',
          }}
        >
          <Grid container spacing={3}>
            {stats.map((stat, index) => (
              <Grid size={{ xs: 6, sm: 3 }} key={index}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography 
                    variant="h3" 
                    component="div" 
                    sx={{ 
                      fontWeight: 'bold', 
                      color: '#2e7d32',
                      mb: 1
                    }}
                  >
                    {stat.number}
                  </Typography>
                  <Typography variant="body1" color="text.secondary">
                    {stat.label}
                  </Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
        </Paper>

        {/* Features Section */}
        <Box sx={{ mb: 8 }}>
          <Typography
            variant="h3"
            component="h2"
            gutterBottom
            sx={{ 
              textAlign: 'center', 
              mb: 6, 
              fontWeight: 'bold', 
              color: '#2e7d32',
              fontSize: { xs: '2rem', md: '3rem' }
            }}
          >
            Tại sao chọn FruitStore?
          </Typography>
          <Grid container spacing={4}>
            {features.map((feature, index) => (
              <Grid size={{ xs: 12, sm: 6, md: 4 }} key={index}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    textAlign: 'center',
                    p: 3,
                    borderRadius: 3,
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-8px)',
                      boxShadow: 6,
                      backgroundColor: '#f8f9fa',
                    },
                  }}
                >
                  <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <Box sx={{ mb: 3, p: 2, borderRadius: '50%', backgroundColor: '#e8f5e8' }}>
                      {feature.icon}
                    </Box>
                    <Typography variant="h5" component="h3" gutterBottom sx={{ fontWeight: 'bold', mb: 2 }}>
                      {feature.title}
                    </Typography>
                    <Typography variant="body1" color="text.secondary" sx={{ lineHeight: 1.6 }}>
                      {feature.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>

        {/* Testimonials Section */}
        <Box sx={{ mb: 8 }}>
          <Typography
            variant="h3"
            component="h2"
            gutterBottom
            sx={{ 
              textAlign: 'center', 
              mb: 6, 
              fontWeight: 'bold', 
              color: '#2e7d32',
              fontSize: { xs: '2rem', md: '3rem' }
            }}
          >
            Khách hàng nói gì về chúng tôi?
          </Typography>
          <Grid container spacing={4}>
            {testimonials.map((testimonial, index) => (
              <Grid size={{ xs: 12, md: 4 }} key={index}>
                <Paper
                  elevation={2}
                  sx={{
                    p: 4,
                    height: '100%',
                    borderRadius: 3,
                    backgroundColor: '#fafafa',
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: 4,
                    },
                  }}
                >
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                    <Avatar
                      sx={{
                        bgcolor: '#2e7d32',
                        mr: 2,
                        width: 50,
                        height: 50,
                        fontSize: '1.2rem',
                        fontWeight: 'bold',
                      }}
                    >
                      {testimonial.avatar}
                    </Avatar>
                    <Box>
                      <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                        {testimonial.name}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {testimonial.location}
                      </Typography>
                    </Box>
                  </Box>
                  <Box sx={{ display: 'flex', mb: 2 }}>
                    {[...Array(testimonial.rating)].map((_, i) => (
                      <Star key={i} sx={{ color: '#ffc107', fontSize: 20 }} />
                    ))}
                  </Box>
                  <Typography variant="body1" sx={{ fontStyle: 'italic', lineHeight: 1.6 }}>
                    "{testimonial.comment}"
                  </Typography>
                </Paper>
              </Grid>
            ))}
          </Grid>
        </Box>

        {/* Newsletter Section */}
        <Paper
          elevation={3}
          sx={{
            p: 6,
            mb: 6,
            textAlign: 'center',
            background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
            borderRadius: 3,
          }}
        >
          <Typography variant="h4" component="h2" gutterBottom sx={{ fontWeight: 'bold', color: '#2e7d32', mb: 2 }}>
            🎁 Đăng ký nhận ưu đãi đặc biệt
          </Typography>
          <Typography variant="body1" sx={{ mb: 4, color: 'text.secondary', maxWidth: 600, mx: 'auto' }}>
            Nhận thông tin về các sản phẩm mới, khuyến mãi hấp dẫn và mẹo chăm sóc sức khỏe từ các chuyên gia dinh dưỡng
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap', maxWidth: 500, mx: 'auto' }}>
            <Button
              variant="contained"
              size="large"
              sx={{
                backgroundColor: '#2e7d32',
                fontWeight: 'bold',
                px: 4,
                py: 1.5,
                flexGrow: 1,
                minWidth: 200,
                '&:hover': {
                  backgroundColor: '#1b5e20',
                  transform: 'translateY(-2px)',
                },
                transition: 'all 0.3s',
              }}
            >
              Đăng ký ngay
            </Button>
          </Box>
          <Box sx={{ mt: 3, display: 'flex', justifyContent: 'center', gap: 1, flexWrap: 'wrap' }}>
            <Chip label="Miễn phí vận chuyển" color="success" variant="outlined" />
            <Chip label="Ưu đãi độc quyền" color="success" variant="outlined" />
            <Chip label="Hủy bất kỳ lúc nào" color="success" variant="outlined" />
          </Box>
        </Paper>

        {/* Call to Action */}
        <Paper
          elevation={4}
          sx={{
            p: 8,
            textAlign: 'center',
            background: 'linear-gradient(135deg, #2e7d32 0%, #1b5e20 100%)',
            color: 'white',
            borderRadius: 3,
            mb: 4,
          }}
        >
          <Typography 
            variant="h3" 
            component="h2" 
            gutterBottom 
            sx={{ 
              fontWeight: 'bold', 
              mb: 3,
              fontSize: { xs: '2rem', md: '3rem' }
            }}
          >
            Bắt đầu mua sắm ngay hôm nay!
          </Typography>
          <Typography 
            variant="h6" 
            sx={{ 
              mb: 6, 
              maxWidth: 600, 
              mx: 'auto',
              lineHeight: 1.6,
              opacity: 0.9
            }}
          >
            Khám phá bộ sưu tập trái cây tươi ngon của chúng tôi và tận hưởng trải nghiệm mua sắm tuyệt vời
          </Typography>
          <Box sx={{ display: 'flex', gap: 3, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              size="large"
              sx={{
                backgroundColor: 'white',
                color: '#2e7d32',
                fontWeight: 'bold',
                px: 6,
                py: 2,
                fontSize: '1.1rem',
                '&:hover': {
                  backgroundColor: '#f5f5f5',
                  transform: 'translateY(-2px)',
                },
                transition: 'all 0.3s',
              }}
            >
              Xem tất cả sản phẩm
            </Button>
            <Button
              variant="outlined"
              size="large"
              sx={{
                borderColor: 'white',
                color: 'white',
                fontWeight: 'bold',
                px: 6,
                py: 2,
                fontSize: '1.1rem',
                '&:hover': {
                  backgroundColor: 'rgba(255,255,255,0.1)',
                  borderColor: 'white',
                  transform: 'translateY(-2px)',
                },
                transition: 'all 0.3s',
              }}
            >
              Liên hệ tư vấn
            </Button>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
};

export default HomePage;
