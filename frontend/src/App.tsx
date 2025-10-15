import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Layout from './components/layout/Layout';
import HomePage from './pages/HomePage';

// Create Material-UI theme
const theme = createTheme({
  palette: {
    primary: {
      main: '#2e7d32',
      light: '#4caf50',
      dark: '#1b5e20',
    },
    secondary: {
      main: '#ff9800',
    },
    background: {
      default: '#fafafa',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 'bold',
    },
    h2: {
      fontWeight: 'bold',
    },
    h3: {
      fontWeight: 'bold',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
        },
      },
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/products" element={<div>Sản phẩm (Coming Soon)</div>} />
            <Route path="/cart" element={<div>Giỏ hàng (Coming Soon)</div>} />
            <Route path="/login" element={<div>Đăng nhập (Coming Soon)</div>} />
            <Route path="/register" element={<div>Đăng ký (Coming Soon)</div>} />
            <Route path="/profile" element={<div>Hồ sơ (Coming Soon)</div>} />
            <Route path="/orders" element={<div>Đơn hàng (Coming Soon)</div>} />
            <Route path="/admin" element={<div>Quản trị (Coming Soon)</div>} />
          </Routes>
        </Layout>
      </Router>
    </ThemeProvider>
  );
}

export default App;
