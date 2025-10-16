// Environment configuration
export const config = {
  // API Configuration
  api: {
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    timeout: parseInt(import.meta.env.VITE_API_TIMEOUT || '10000'),
    retryAttempts: parseInt(import.meta.env.VITE_API_RETRY_ATTEMPTS || '3'),
  },

  // App Configuration
  app: {
    name: import.meta.env.VITE_APP_NAME || 'FruitStore',
    version: import.meta.env.VITE_APP_VERSION || '1.0.0',
    description: import.meta.env.VITE_APP_DESCRIPTION || 'Fresh Fruits Online Store',
    environment: import.meta.env.VITE_NODE_ENV || 'development',
  },

  // Feature Flags
  features: {
    analytics: import.meta.env.VITE_ENABLE_ANALYTICS === 'true',
    debugTools: import.meta.env.VITE_ENABLE_DEBUG_TOOLS === 'true',
  },

  // Development
  isDevelopment: import.meta.env.DEV,
  isProduction: import.meta.env.PROD,
  debug: import.meta.env.VITE_DEBUG === 'true',
};

// Validate required environment variables
const validateConfig = () => {
  const required = ['VITE_API_BASE_URL'];
  const missing = required.filter(key => !import.meta.env[key]);
  
  if (missing.length > 0) {
    console.warn(`Missing environment variables: ${missing.join(', ')}`);
    console.warn('Using default values. Please check your .env file.');
  }
};

// Run validation in development
if (config.isDevelopment) {
  validateConfig();
}

export default config;
