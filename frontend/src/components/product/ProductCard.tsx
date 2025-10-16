  import React from 'react';
import {
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Typography,
  Button,
  Box,
  Chip,
  IconButton,
  Badge,
} from '@mui/material';
import {
  ShoppingCart,
  Visibility,
  Star,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { type Product } from '../../services/productService';
import { useCartStore } from '../../stores/cartStore';

interface ProductCardProps {
  product: Product;
  onAddToCart?: (productId: number) => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onAddToCart }) => {
  const navigate = useNavigate();
  const { addToCart, isLoading } = useCartStore();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await addToCart(product.id, 1);
      if (onAddToCart) {
        onAddToCart(product.id);
      }
    } catch (error) {
      // Error is handled by the store
    }
  };

  const handleViewDetails = () => {
    navigate(`/products/${product.id}`);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const isInStock = product.stock > 0;

  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
        '&:hover': {
          transform: 'translateY(-4px)',
          boxShadow: 4,
        },
        cursor: 'pointer',
      }}
      onClick={handleViewDetails}
    >
      <Box sx={{ position: 'relative' }}>
        <CardMedia
          component="img"
          height="200"
          image={product.imageUrl || '/api/placeholder/300/200'}
          alt={product.name}
          sx={{
            objectFit: 'cover',
            backgroundColor: '#f5f5f5',
          }}
        />
        {!isInStock && (
          <Box
            sx={{
              position: 'absolute',
              top: 8,
              right: 8,
              backgroundColor: 'rgba(0, 0, 0, 0.7)',
              color: 'white',
              padding: '4px 8px',
              borderRadius: 1,
              fontSize: '0.75rem',
              fontWeight: 'bold',
            }}
          >
            Hết hàng
          </Box>
        )}
        {product.stock > 0 && product.stock <= 10 && (
          <Chip
            label="Sắp hết hàng"
            color="warning"
            size="small"
            sx={{
              position: 'absolute',
              top: 8,
              right: 8,
            }}
          />
        )}
      </Box>

      <CardContent sx={{ flexGrow: 1, pb: 1 }}>
        <Typography
          variant="h6"
          component="h3"
          sx={{
            fontWeight: 'bold',
            fontSize: '1.1rem',
            lineHeight: 1.2,
            mb: 1,
            display: '-webkit-box',
            WebkitLineClamp: 2,
            WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
          }}
        >
          {product.name}
        </Typography>

        <Typography
          variant="body2"
          color="text.secondary"
          sx={{
            mb: 1,
            display: '-webkit-box',
            WebkitLineClamp: 2,
            WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
            minHeight: '2.5em',
          }}
        >
          {product.description}
        </Typography>

        <Box display="flex" alignItems="center" mb={1}>
          <Chip
            label={product.category.name}
            size="small"
            color="primary"
            variant="outlined"
            sx={{ fontSize: '0.75rem' }}
          />
        </Box>

        <Box display="flex" alignItems="center" justifyContent="space-between">
          <Typography
            variant="h6"
            color="primary"
            sx={{
              fontWeight: 'bold',
              fontSize: '1.2rem',
            }}
          >
            {formatPrice(product.price)}
          </Typography>
          
          <Box display="flex" alignItems="center">
            <Typography variant="body2" color="text.secondary" sx={{ mr: 0.5 }}>
              Còn lại:
            </Typography>
            <Typography
              variant="body2"
              color={product.stock > 10 ? 'success.main' : product.stock > 0 ? 'warning.main' : 'error.main'}
              fontWeight="bold"
            >
              {product.stock}
            </Typography>
          </Box>
        </Box>
      </CardContent>

      <CardActions sx={{ p: 2, pt: 0 }}>
        <Button
          fullWidth
          variant="contained"
          startIcon={<ShoppingCart />}
          onClick={handleAddToCart}
          disabled={!isInStock || isLoading}
          sx={{
            py: 1,
            fontWeight: 'bold',
            textTransform: 'none',
          }}
        >
          {isLoading ? 'Đang thêm...' : isInStock ? 'Thêm vào giỏ' : 'Hết hàng'}
        </Button>
        
        <IconButton
          onClick={(e) => {
            e.stopPropagation();
            handleViewDetails();
          }}
          sx={{
            ml: 1,
            backgroundColor: 'primary.light',
            color: 'white',
            '&:hover': {
              backgroundColor: 'primary.main',
            },
          }}
        >
          <Visibility />
        </IconButton>
      </CardActions>
    </Card>
  );
};

export default ProductCard;
