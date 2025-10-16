import React from 'react';
import {
  Paper,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Slider,
  Box,
  Button,
  Chip,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Divider,
} from '@mui/material';
import {
  ExpandMore,
  Search,
  Clear,
  FilterList,
} from '@mui/icons-material';
import { type ProductFilters as ProductFiltersType, type Category } from '../../services/productService';

interface ProductFiltersProps {
  filters: ProductFiltersType;
  categories: Category[];
  onFiltersChange: (filters: Partial<ProductFiltersType>) => void;
  onClearFilters: () => void;
  isLoading?: boolean;
}

const ProductFilters: React.FC<ProductFiltersProps> = ({
  filters,
  categories,
  onFiltersChange,
  onClearFilters,
  isLoading = false,
}) => {
  const [expanded, setExpanded] = React.useState<string | false>('filters');

  const handleExpandChange = (panel: string) => (event: React.SyntheticEvent, isExpanded: boolean) => {
    setExpanded(isExpanded ? panel : false);
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onFiltersChange({ search: event.target.value, page: 0 });
  };

  const handleCategoryChange = (event: any) => {
    onFiltersChange({ categoryId: event.target.value, page: 0 });
  };

  const handlePriceChange = (event: Event, newValue: number | number[]) => {
    const [min, max] = newValue as number[];
    onFiltersChange({ 
      minPrice: min, 
      maxPrice: max, 
      page: 0 
    });
  };

  const handleSortChange = (event: any) => {
    onFiltersChange({ sortBy: event.target.value, page: 0 });
  };

  const handleSortDirectionChange = (event: any) => {
    onFiltersChange({ sortDirection: event.target.value, page: 0 });
  };

  const hasActiveFilters = () => {
    return !!(
      filters.search ||
      filters.categoryId ||
      filters.minPrice ||
      filters.maxPrice ||
      filters.sortBy !== 'createdAt' ||
      filters.sortDirection !== 'desc'
    );
  };

  const getActiveFiltersCount = () => {
    let count = 0;
    if (filters.search) count++;
    if (filters.categoryId) count++;
    if (filters.minPrice || filters.maxPrice) count++;
    if (filters.sortBy !== 'createdAt' || filters.sortDirection !== 'desc') count++;
    return count;
  };

  return (
    <Paper elevation={2} sx={{ p: 2, mb: 3 }}>
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
        <Box display="flex" alignItems="center">
          <FilterList sx={{ mr: 1 }} />
          <Typography variant="h6" fontWeight="bold">
            Bộ lọc
          </Typography>
          {hasActiveFilters() && (
            <Chip
              label={getActiveFiltersCount()}
              color="primary"
              size="small"
              sx={{ ml: 1 }}
            />
          )}
        </Box>
        {hasActiveFilters() && (
          <Button
            startIcon={<Clear />}
            onClick={onClearFilters}
            size="small"
            color="error"
          >
            Xóa bộ lọc
          </Button>
        )}
      </Box>

      <Accordion
        expanded={expanded === 'filters'}
        onChange={handleExpandChange('filters')}
      >
        <AccordionSummary expandIcon={<ExpandMore />}>
          <Typography variant="subtitle1" fontWeight="medium">
            Tìm kiếm và lọc sản phẩm
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Box display="flex" flexDirection="column" gap={3}>
            {/* Search */}
            <TextField
              fullWidth
              label="Tìm kiếm sản phẩm"
              placeholder="Nhập tên sản phẩm..."
              value={filters.search || ''}
              onChange={handleSearchChange}
              InputProps={{
                startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />,
              }}
              disabled={isLoading}
            />

            {/* Category Filter */}
            <FormControl fullWidth>
              <InputLabel>Danh mục</InputLabel>
              <Select
                value={filters.categoryId || ''}
                onChange={handleCategoryChange}
                label="Danh mục"
                disabled={isLoading}
              >
                <MenuItem value="">
                  <em>Tất cả danh mục</em>
                </MenuItem>
                {categories.map((category) => (
                  <MenuItem key={category.id} value={category.id}>
                    {category.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {/* Price Range */}
            <Box>
              <Typography gutterBottom>
                Khoảng giá: {filters.minPrice ? `${filters.minPrice.toLocaleString()}đ` : '0đ'} - {filters.maxPrice ? `${filters.maxPrice.toLocaleString()}đ` : '10,000,000đ'}
              </Typography>
              <Slider
                value={[filters.minPrice || 0, filters.maxPrice || 10000000]}
                onChange={handlePriceChange}
                valueLabelDisplay="auto"
                min={0}
                max={10000000}
                step={100000}
                valueLabelFormat={(value) => `${value.toLocaleString()}đ`}
                disabled={isLoading}
                sx={{ mt: 2 }}
              />
            </Box>

            <Divider />

            {/* Sort Options */}
            <Box display="flex" gap={2} flexWrap="wrap">
              <FormControl sx={{ minWidth: 150 }}>
                <InputLabel>Sắp xếp theo</InputLabel>
                <Select
                  value={filters.sortBy || 'createdAt'}
                  onChange={handleSortChange}
                  label="Sắp xếp theo"
                  disabled={isLoading}
                >
                  <MenuItem value="createdAt">Ngày tạo</MenuItem>
                  <MenuItem value="name">Tên sản phẩm</MenuItem>
                  <MenuItem value="price">Giá</MenuItem>
                </Select>
              </FormControl>

              <FormControl sx={{ minWidth: 120 }}>
                <InputLabel>Thứ tự</InputLabel>
                <Select
                  value={filters.sortDirection || 'desc'}
                  onChange={handleSortDirectionChange}
                  label="Thứ tự"
                  disabled={isLoading}
                >
                  <MenuItem value="asc">Tăng dần</MenuItem>
                  <MenuItem value="desc">Giảm dần</MenuItem>
                </Select>
              </FormControl>
            </Box>
          </Box>
        </AccordionDetails>
      </Accordion>
    </Paper>
  );
};

export default ProductFilters;
