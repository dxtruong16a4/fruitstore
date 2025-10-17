import React from 'react';
import Header from './Header';
import Footer from './Footer';
import HeroSection from '../HeroSection';

interface PageLayoutProps {
  children: React.ReactNode;
  title?: string;
  subtitle?: string;
  showHero?: boolean;
  showHeader?: boolean;
  showFooter?: boolean;
  heroProps?: {
    showSearch?: boolean;
    searchPlaceholder?: string;
    onSearch?: (query: string) => void;
  };
  footerProps?: {
    showFullFooter?: boolean;
  };
  className?: string;
}

const PageLayout: React.FC<PageLayoutProps> = ({
  children,
  title,
  subtitle,
  showHero = false,
  showHeader = true,
  showFooter = true,
  heroProps = {},
  footerProps = {},
  className = ''
}) => {
  return (
    <div className={`min-h-screen bg-gradient-to-b from-green-50 to-white ${className}`}>
      {showHeader && <Header />}
      
      {showHero && title && (
        <HeroSection
          title={title}
          subtitle={subtitle}
          {...heroProps}
        />
      )}
      
      <main className="flex-1">
        {children}
      </main>
      
      {showFooter && <Footer {...footerProps} />}
    </div>
  );
};

export default PageLayout;
