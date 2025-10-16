import React from 'react';
import { Box, Container, Typography, Link, IconButton } from '@mui/material';
import { Facebook, Twitter, Instagram, Email, Phone, LocationOn } from '@mui/icons-material';

const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        backgroundColor: 'primary.main',
        color: 'primary.contrastText',
        py: 4,
        mt: 'auto',
      }}
    >
      <Container maxWidth="lg">
        <Box 
          sx={{ 
            display: 'grid', 
            gridTemplateColumns: { 
              xs: '1fr', 
              sm: 'repeat(2, 1fr)', 
              md: 'repeat(4, 1fr)' 
            }, 
            gap: 4 
          }}
        >
          {/* Company Info */}
          <Box>
            <Typography variant="h6" gutterBottom>
              🍎 FruitStore
            </Typography>
            <Typography variant="body2" paragraph>
              Fresh fruits delivered to your doorstep. Quality guaranteed, 
              satisfaction assured.
            </Typography>
            <Box sx={{ display: 'flex', gap: 1 }}>
              <IconButton color="inherit" size="small">
                <Facebook />
              </IconButton>
              <IconButton color="inherit" size="small">
                <Twitter />
              </IconButton>
              <IconButton color="inherit" size="small">
                <Instagram />
              </IconButton>
            </Box>
          </Box>

          {/* Quick Links */}
          <Box>
            <Typography variant="h6" gutterBottom>
              Quick Links
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Link href="/" color="inherit" underline="hover">
                Home
              </Link>
              <Link href="/products" color="inherit" underline="hover">
                Products
              </Link>
              <Link href="/cart" color="inherit" underline="hover">
                Cart
              </Link>
              <Link href="/orders" color="inherit" underline="hover">
                Orders
              </Link>
            </Box>
          </Box>

          {/* Customer Service */}
          <Box>
            <Typography variant="h6" gutterBottom>
              Customer Service
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Link href="/contact" color="inherit" underline="hover">
                Contact Us
              </Link>
              <Link href="/faq" color="inherit" underline="hover">
                FAQ
              </Link>
              <Link href="/shipping" color="inherit" underline="hover">
                Shipping Info
              </Link>
              <Link href="/returns" color="inherit" underline="hover">
                Returns
              </Link>
            </Box>
          </Box>

          {/* Contact Info */}
          <Box>
            <Typography variant="h6" gutterBottom>
              Contact Info
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Email fontSize="small" />
                <Typography variant="body2">
                  info@fruitstore.com
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Phone fontSize="small" />
                <Typography variant="body2">
                  +1 (555) 123-4567
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <LocationOn fontSize="small" />
                <Typography variant="body2">
                  123 Fruit Street, City, State 12345
                </Typography>
              </Box>
            </Box>
          </Box>
        </Box>

        <Box sx={{ borderTop: 1, borderColor: 'divider', mt: 3, pt: 3 }}>
          <Box 
            sx={{ 
              display: 'flex', 
              flexDirection: { xs: 'column', md: 'row' }, 
              justifyContent: 'space-between', 
              alignItems: 'center', 
              gap: 2 
            }}
          >
            <Typography variant="body2" color="inherit">
              © {currentYear} FruitStore. All rights reserved.
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: { xs: 'flex-start', md: 'flex-end' } }}>
              <Link href="/privacy" color="inherit" underline="hover" variant="body2">
                Privacy Policy
              </Link>
              <Link href="/terms" color="inherit" underline="hover" variant="body2">
                Terms of Service
              </Link>
              <Link href="/cookies" color="inherit" underline="hover" variant="body2">
                Cookie Policy
              </Link>
            </Box>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
