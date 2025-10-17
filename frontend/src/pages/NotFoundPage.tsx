import React from 'react';
import { Container, Typography, Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm" sx={{ mt: 8, mb: 4 }}>
      <Box sx={{ textAlign: 'center' }}>
        <Typography variant="h1" component="h1" gutterBottom>
          404
        </Typography>
        <Typography variant="h4" component="h2" gutterBottom>
          Trang không tìm thấy
        </Typography>
        <Typography variant="body1" color="text.secondary" paragraph>
          Trang bạn đang tìm kiếm không tồn tại.
        </Typography>
        <Button
          variant="contained"
          onClick={() => navigate('/')}
          sx={{ mt: 2 }}
        >
          Quay về trang chủ
        </Button>
      </Box>
    </Container>
  );
};

export default NotFoundPage;
