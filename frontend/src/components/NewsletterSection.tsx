import React from 'react';
import { Apple } from 'lucide-react';

interface NewsletterSectionProps {
  title?: string;
  subtitle?: string;
  placeholder?: string;
  buttonText?: string;
  onSubscribe?: (email: string) => void;
  className?: string;
}

const NewsletterSection: React.FC<NewsletterSectionProps> = ({
  title = "Get Fresh Updates",
  subtitle = "Subscribe to our newsletter for exclusive deals and health tips",
  placeholder = "Enter your email",
  buttonText = "Subscribe",
  onSubscribe,
  className = ''
}) => {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData(e.target as HTMLFormElement);
    const email = formData.get('email') as string;
    
    if (onSubscribe) {
      onSubscribe(email);
    } else {
      // Default behavior
      console.log('Newsletter subscription:', email);
      alert('Thank you for subscribing!');
    }
  };

  return (
    <section className={`py-20 bg-gradient-to-r from-green-600 to-emerald-600 ${className}`}>
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <Apple className="w-16 h-16 text-white mx-auto mb-6" />
        <h2 className="text-4xl font-bold text-white mb-4">
          {title}
        </h2>
        <p className="text-green-100 text-lg mb-8">
          {subtitle}
        </p>
        <form onSubmit={handleSubmit} className="flex flex-col sm:flex-row gap-4 max-w-md mx-auto">
          <input
            type="email"
            name="email"
            placeholder={placeholder}
            required
            className="flex-1 px-6 py-4 rounded-full text-white placeholder-gray-300 bg-gray-800 focus:outline-none focus:ring-4 focus:ring-green-300"
          />
          <button 
            type="submit"
            className="bg-white text-green-600 px-8 py-4 rounded-full font-semibold hover:bg-green-50 transition transform hover:scale-105 shadow-lg"
          >
            {buttonText}
          </button>
        </form>
      </div>
    </section>
  );
};

export default NewsletterSection;
