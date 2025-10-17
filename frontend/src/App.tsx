import React, { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { useAppDispatch } from './redux';
import { restoreAuth } from './redux/thunks/authThunks';
import { ThemeProvider } from './theme/ThemeProvider';
import { AppRoutes } from './routes';
import './App.css';

const App: React.FC = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    // Restore authentication state when app starts
    const token = localStorage.getItem('token');
    if (token) {
      dispatch(restoreAuth());
    }
  }, [dispatch]);

  return (
    <ThemeProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
