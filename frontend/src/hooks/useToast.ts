import { useState, useCallback } from 'react';
import { type ToastData } from '../components/ToastContainer';

export const useToast = () => {
  const [toasts, setToasts] = useState<ToastData[]>([]);

  const addToast = useCallback((toast: Omit<ToastData, 'id'>) => {
    const id = Math.random().toString(36).substr(2, 9);
    const newToast: ToastData = {
      ...toast,
      id
    };

    setToasts(prev => [...prev, newToast]);
  }, []);

  const removeToast = useCallback((id: string) => {
    setToasts(prev => prev.filter(toast => toast.id !== id));
  }, []);

  const showSuccess = useCallback((title: string, message?: string) => {
    addToast({
      type: 'success',
      title,
      message,
      duration: 4000
    });
  }, [addToast]);

  const showError = useCallback((title: string, message?: string) => {
    addToast({
      type: 'error',
      title,
      message,
      duration: 6000
    });
  }, [addToast]);

  const showWarning = useCallback((title: string, message?: string) => {
    addToast({
      type: 'warning',
      title,
      message,
      duration: 5000
    });
  }, [addToast]);

  const showInfo = useCallback((title: string, message?: string) => {
    addToast({
      type: 'info',
      title,
      message,
      duration: 4000
    });
  }, [addToast]);

  return {
    toasts,
    addToast,
    removeToast,
    showSuccess,
    showError,
    showWarning,
    showInfo
  };
};
