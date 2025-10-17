import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../redux';
import { logout } from '../../redux/slices/authSlice';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Badge,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Divider,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import {
  Menu as MenuIcon,
  ShoppingCart,
  Person,
  Home,
  Store,
  Assignment,
  Login,
  Logout,
} from '@mui/icons-material';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const { isAuthenticated, user } = useAppSelector((state) => state.auth);
  const { totalItems } = useAppSelector((state) => state.cart);

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleLogout = () => {
    dispatch(logout());
    handleMenuClose();
    navigate('/');
  };

  const handleNavigation = (path: string) => {
    navigate(path);
    setMobileOpen(false);
    handleMenuClose();
  };

  const navigationItems = [
    { label: 'Trang chủ', path: '/', icon: <Home /> },
    { label: 'Sản phẩm', path: '/products', icon: <Store /> },
  ];

  const protectedItems = [
    { label: 'Giỏ hàng', path: '/cart', icon: <ShoppingCart />, badge: totalItems },
    { label: 'Đơn hàng', path: '/orders', icon: <Assignment /> },
  ];

  const drawer = (
    <Box>
      <Toolbar />
      <Divider />
      <List>
        {navigationItems.map((item) => (
          <ListItem key={item.label} disablePadding>
            <ListItemButton 
              onClick={() => handleNavigation(item.path)}
              selected={location.pathname === item.path}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          </ListItem>
        ))}
        
        {isAuthenticated && (
          <>
            <Divider />
            {protectedItems.map((item) => (
              <ListItem key={item.label} disablePadding>
                <ListItemButton 
                  onClick={() => handleNavigation(item.path)}
                  selected={location.pathname === item.path}
                >
                  <ListItemIcon>
                    {item.badge ? (
                      <Badge badgeContent={item.badge} color="error">
                        {item.icon}
                      </Badge>
                    ) : (
                      item.icon
                    )}
                  </ListItemIcon>
                  <ListItemText primary={item.label} />
                </ListItemButton>
              </ListItem>
            ))}
          </>
        )}
      </List>
    </Box>
  );

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          {isMobile && (
            <IconButton
              color="inherit"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: 2 }}
            >
              <MenuIcon />
            </IconButton>
          )}
          
          <Typography 
            variant="h6" 
            component="div" 
            sx={{ flexGrow: 1, cursor: 'pointer' }}
            onClick={() => navigate('/')}
          >
            🍎 FruitStore
          </Typography>

          {!isMobile && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              {navigationItems.map((item) => (
                <Button
                  key={item.label}
                  color="inherit"
                  onClick={() => handleNavigation(item.path)}
                  startIcon={item.icon}
                >
                  {item.label}
                </Button>
              ))}

              {isAuthenticated && (
                <>
                  {protectedItems.map((item) => (
                    <Button
                      key={item.label}
                      color="inherit"
                      onClick={() => handleNavigation(item.path)}
                      startIcon={
                        item.badge ? (
                          <Badge badgeContent={item.badge} color="error">
                            {item.icon}
                          </Badge>
                        ) : (
                          item.icon
                        )
                      }
                    >
                      {item.label}
                    </Button>
                  ))}
                </>
              )}

              {isAuthenticated ? (
                <Button
                  color="inherit"
                  onClick={handleMenuOpen}
                  startIcon={<Person />}
                >
                  {user?.username}
                </Button>
              ) : (
                <Button
                  color="inherit"
                  onClick={() => handleNavigation('/login')}
                  startIcon={<Login />}
                >
                  Đăng nhập
                </Button>
              )}
            </Box>
          )}
        </Toolbar>
      </AppBar>

      {/* Mobile Drawer */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        ModalProps={{
          keepMounted: true,
        }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': { boxSizing: 'border-box', width: 240 },
        }}
      >
        {drawer}
      </Drawer>

      {/* User Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
      >
        <MenuItem onClick={() => { handleNavigation('/'); handleMenuClose(); }}>
          <ListItemIcon>
            <Home fontSize="small" />
          </ListItemIcon>
          Trang chủ
        </MenuItem>
        <MenuItem onClick={() => { handleNavigation('/products'); handleMenuClose(); }}>
          <ListItemIcon>
            <Store fontSize="small" />
          </ListItemIcon>
          Sản phẩm
        </MenuItem>
        {isAuthenticated && (
          <>
            <MenuItem onClick={() => { handleNavigation('/cart'); handleMenuClose(); }}>
              <ListItemIcon>
                <ShoppingCart fontSize="small" />
              </ListItemIcon>
              Giỏ hàng
            </MenuItem>
            <MenuItem onClick={() => { handleNavigation('/orders'); handleMenuClose(); }}>
              <ListItemIcon>
                <Assignment fontSize="small" />
              </ListItemIcon>
              Đơn hàng
            </MenuItem>
          </>
        )}
        <Divider />
        {isAuthenticated ? (
          <MenuItem onClick={handleLogout}>
            <ListItemIcon>
              <Logout fontSize="small" />
            </ListItemIcon>
            Đăng xuất
          </MenuItem>
        ) : (
          <MenuItem onClick={() => { handleNavigation('/login'); handleMenuClose(); }}>
            <ListItemIcon>
              <Login fontSize="small" />
            </ListItemIcon>
            Đăng nhập
          </MenuItem>
        )}
      </Menu>
    </>
  );
};

export default Header;
