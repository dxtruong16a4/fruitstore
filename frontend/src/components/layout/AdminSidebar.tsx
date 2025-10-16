import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  ListItemButton,
  Typography,
  Box,
  Divider,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  Dashboard,
  Inventory,
  ShoppingCart,
  People,
  Category,
  LocalOffer,
  Analytics,
  Store,
} from '@mui/icons-material';

interface AdminSidebarProps {
  mobileOpen: boolean;
  onDrawerToggle: () => void;
}

const AdminSidebar: React.FC<AdminSidebarProps> = ({ mobileOpen, onDrawerToggle }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const adminMenuItems = [
    {
      label: 'Dashboard',
      path: '/admin',
      icon: <Dashboard />,
      description: 'Overview and statistics'
    },
    {
      label: 'Products',
      path: '/admin/products',
      icon: <Inventory />,
      description: 'Manage product catalog'
    },
    {
      label: 'Orders',
      path: '/admin/orders',
      icon: <ShoppingCart />,
      description: 'Process customer orders'
    },
    {
      label: 'Users',
      path: '/admin/users',
      icon: <People />,
      description: 'Manage user accounts'
    },
    {
      label: 'Categories',
      path: '/admin/categories',
      icon: <Category />,
      description: 'Organize product categories'
    },
    {
      label: 'Discounts',
      path: '/admin/discounts',
      icon: <LocalOffer />,
      description: 'Manage discount codes'
    },
    {
      label: 'Analytics',
      path: '/admin/analytics',
      icon: <Analytics />,
      description: 'Sales and performance data'
    },
  ];

  const handleNavigation = (path: string) => {
    navigate(path);
    if (isMobile) {
      onDrawerToggle();
    }
  };

  const drawer = (
    <Box>
      <Box sx={{ p: 2, textAlign: 'center' }}>
        <Typography variant="h6" component="div" sx={{ fontWeight: 'bold' }}>
          🔧 Admin Panel
        </Typography>
        <Typography variant="body2" color="text.secondary">
          FruitStore Management
        </Typography>
      </Box>
      <Divider />
      
      <List>
        {adminMenuItems.map((item) => (
          <ListItem key={item.label} disablePadding>
            <ListItemButton
              onClick={() => handleNavigation(item.path)}
              selected={location.pathname === item.path}
              sx={{
                '&.Mui-selected': {
                  backgroundColor: theme.palette.primary.main,
                  color: theme.palette.primary.contrastText,
                  '&:hover': {
                    backgroundColor: theme.palette.primary.dark,
                  },
                  '& .MuiListItemIcon-root': {
                    color: theme.palette.primary.contrastText,
                  },
                },
              }}
            >
              <ListItemIcon>
                {item.icon}
              </ListItemIcon>
              <ListItemText 
                primary={item.label}
                secondary={item.description}
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      
      <Divider />
      
      <List>
        <ListItem disablePadding>
          <ListItemButton onClick={() => navigate('/')}>
            <ListItemIcon>
              <Store />
            </ListItemIcon>
            <ListItemText primary="Back to Store" />
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  );

  return (
    <Drawer
      variant={isMobile ? 'temporary' : 'permanent'}
      open={isMobile ? mobileOpen : true}
      onClose={onDrawerToggle}
      ModalProps={{
        keepMounted: true,
      }}
      sx={{
        width: 240,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: 240,
          boxSizing: 'border-box',
          position: 'relative',
          height: '100%',
        },
        display: { xs: 'block', md: 'block' },
      }}
    >
      {drawer}
    </Drawer>
  );
};

export default AdminSidebar;
