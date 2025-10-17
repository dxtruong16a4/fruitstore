import React from 'react';
import { Box, Container } from '@mui/material';
// import Navigation from '../components/layout/Navigation';
import Footer from '../components/layout/Footer';

interface UserLayoutProps {
  children: React.ReactNode;
}

const UserLayout: React.FC<UserLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen bg-gradient-to-b from-green-50 to-white">
      {/* <Navigation /> */}
      <main className="flex-1">
        <Container maxWidth="lg" sx={{ py: 3 }}>
          {children}
        </Container>
      </main>
      <Footer />
    </div>
  );
};

export default UserLayout;
