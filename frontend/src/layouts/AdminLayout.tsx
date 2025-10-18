import React, { useState } from 'react';
import { Box } from '@mui/material';
import AdminHeader from '../components/layout/AdminHeader';
import AdminSidebar from '../components/layout/AdminSidebar';

interface AdminLayoutProps {
  children: React.ReactNode;
}

const AdminLayout: React.FC<AdminLayoutProps> = ({ children }) => {
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      {/* Admin Header */}
      <AdminHeader onDrawerToggle={handleDrawerToggle} />
      
      {/* Admin Sidebar */}
      <AdminSidebar 
        mobileOpen={mobileOpen}
        onDrawerToggle={handleDrawerToggle}
      />
      
      {/* Main Content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: { md: `calc(100% - ${240}px)` },
          ml: { md: `${240}px` },
          mt: { xs: '64px', md: 0 },
        }}
      >
        {children}
      </Box>
    </Box>
  );
};

export default AdminLayout;
