import React from 'react';
import { Container, Typography, Box, Card, CardContent, Button, Tabs, Tab } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import RoutingDemo from '../components/RoutingDemo';
import LayoutDemo from '../components/LayoutDemo';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [tabValue, setTabValue] = React.useState(0);

  const handleTabChange = (_: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom>
          🍎 Welcome to FruitStore
        </Typography>
        <Typography variant="h6" color="text.secondary" paragraph>
          Fresh fruits delivered to your doorstep
        </Typography>
      </Box>

      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs value={tabValue} onChange={handleTabChange} aria-label="home tabs">
          <Tab label="Quick Actions" />
          <Tab label="Routing Demo" />
          <Tab label="Layout Demo" />
        </Tabs>
      </Box>

      <TabPanel value={tabValue} index={0}>
        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 3, mb: 4 }}>
          <Card>
            <CardContent>
              <Typography variant="h5" gutterBottom>
                🛒 Shop Now
              </Typography>
              <Typography variant="body1" paragraph>
                Browse our wide selection of fresh fruits and add them to your cart.
              </Typography>
              <Button 
                variant="contained" 
                onClick={() => navigate('/products')}
                fullWidth
              >
                View Products
              </Button>
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Typography variant="h5" gutterBottom>
                📦 My Orders
              </Typography>
              <Typography variant="body1" paragraph>
                Track your orders and view order history.
              </Typography>
              <Button 
                variant="contained" 
                onClick={() => navigate('/orders')}
                fullWidth
              >
                View Orders
              </Button>
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Typography variant="h5" gutterBottom>
                🛒 My Cart
              </Typography>
              <Typography variant="body1" paragraph>
                Review items in your cart and proceed to checkout.
              </Typography>
              <Button 
                variant="contained" 
                onClick={() => navigate('/cart')}
                fullWidth
              >
                View Cart
              </Button>
            </CardContent>
          </Card>
        </Box>

        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              🎯 Phase 1.6 Features
            </Typography>
            <Typography variant="body1" paragraph>
              Complete layout system with responsive design, including user and admin layouts.
              Test different layouts and responsive behavior across all screen sizes.
            </Typography>
          </CardContent>
        </Card>
      </TabPanel>

      <TabPanel value={tabValue} index={1}>
        <RoutingDemo />
      </TabPanel>

      <TabPanel value={tabValue} index={2}>
        <LayoutDemo />
      </TabPanel>
    </Container>
  );
};

export default HomePage;
