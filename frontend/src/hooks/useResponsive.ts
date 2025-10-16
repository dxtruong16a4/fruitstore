import { useState, useEffect } from 'react';
import { breakpoints, type Breakpoint } from '../utils/responsive';

// Hook to get current breakpoint
export const useResponsive = () => {
  const [currentBreakpoint, setCurrentBreakpoint] = useState<Breakpoint>('xs');

  useEffect(() => {
    const updateBreakpoint = () => {
      const width = window.innerWidth;
      
      if (width >= breakpoints.xl) {
        setCurrentBreakpoint('xl');
      } else if (width >= breakpoints.lg) {
        setCurrentBreakpoint('lg');
      } else if (width >= breakpoints.md) {
        setCurrentBreakpoint('md');
      } else if (width >= breakpoints.sm) {
        setCurrentBreakpoint('sm');
      } else {
        setCurrentBreakpoint('xs');
      }
    };

    // Set initial breakpoint
    updateBreakpoint();

    // Add event listener
    window.addEventListener('resize', updateBreakpoint);

    // Cleanup
    return () => window.removeEventListener('resize', updateBreakpoint);
  }, []);

  return {
    currentBreakpoint,
    isXs: currentBreakpoint === 'xs',
    isSm: currentBreakpoint === 'sm',
    isMd: currentBreakpoint === 'md',
    isLg: currentBreakpoint === 'lg',
    isXl: currentBreakpoint === 'xl',
    isMobile: currentBreakpoint === 'xs',
    isTablet: currentBreakpoint === 'sm' || currentBreakpoint === 'md',
    isDesktop: currentBreakpoint === 'lg' || currentBreakpoint === 'xl',
  };
};

// Hook to check if screen is mobile
export const useIsMobile = () => {
  const { isMobile } = useResponsive();
  return isMobile;
};

// Hook to check if screen is tablet
export const useIsTablet = () => {
  const { isTablet } = useResponsive();
  return isTablet;
};

// Hook to check if screen is desktop
export const useIsDesktop = () => {
  const { isDesktop } = useResponsive();
  return isDesktop;
};
