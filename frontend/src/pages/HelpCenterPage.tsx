import React, { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { HelpCategories, ContactSupport } from '../components';
import { PageLayout } from '../components/layout';

const HelpCenterPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [expandedFAQ, setExpandedFAQ] = useState<number | null>(null);

  const faqs = [
    {
      id: 1,
      question: "How do I place an order?",
      answer: "Placing an order is easy! Simply browse our selection of fresh fruits, add items to your cart, and proceed to checkout. You can create an account for faster checkout or checkout as a guest."
    },
    {
      id: 2,
      question: "What are your delivery options?",
      answer: "We offer same-day delivery for orders placed before 2 PM, and next-day delivery for all other orders. Delivery is free on orders over $50. We also offer scheduled delivery for your convenience."
    },
    {
      id: 3,
      question: "How do I track my order?",
      answer: "Once your order is confirmed, you'll receive a tracking number via email. You can also track your order by logging into your account and visiting the 'My Orders' section."
    },
    {
      id: 4,
      question: "What if I'm not satisfied with my fruits?",
      answer: "We stand behind the quality of our products. If you're not completely satisfied, contact us within 24 hours and we'll provide a full refund or replacement."
    },
    {
      id: 5,
      question: "Do you offer organic fruits?",
      answer: "Yes! We have a wide selection of certified organic fruits from local farms. Look for the 'Organic' label on our product pages."
    },
    {
      id: 6,
      question: "How should I store my fruits?",
      answer: "Most fruits should be stored in the refrigerator to maintain freshness. Bananas and some tropical fruits can be stored at room temperature. Check individual product pages for specific storage instructions."
    },
    {
      id: 7,
      question: "Can I modify or cancel my order?",
      answer: "Orders can be modified or cancelled within 30 minutes of placement. After that, please contact our customer service team for assistance."
    },
    {
      id: 8,
      question: "What payment methods do you accept?",
      answer: "We accept all major credit cards (Visa, MasterCard, American Express), PayPal, and Apple Pay for your convenience."
    }
  ];

  const helpCategories = [
    {
      title: "Ordering & Payment",
      icon: "🛒",
      topics: ["How to place an order", "Payment methods", "Order confirmation", "Guest checkout"]
    },
    {
      title: "Delivery & Shipping",
      icon: "🚚",
      topics: ["Delivery options", "Shipping costs", "Delivery times", "Track your order"]
    },
    {
      title: "Returns & Refunds",
      icon: "↩️",
      topics: ["Return policy", "How to return items", "Refund process", "Quality guarantee"]
    },
    {
      title: "Account & Profile",
      icon: "👤",
      topics: ["Create account", "Login issues", "Update profile", "Order history"]
    },
    {
      title: "Products & Quality",
      icon: "🍎",
      topics: ["Product information", "Organic options", "Storage tips", "Seasonal availability"]
    },
    {
      title: "Technical Support",
      icon: "💻",
      topics: ["Website issues", "Mobile app", "Browser compatibility", "Account security"]
    }
  ];

  const filteredFAQs = faqs.filter(faq => 
    faq.question.toLowerCase().includes(searchQuery.toLowerCase()) ||
    faq.answer.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const toggleFAQ = (id: number) => {
    setExpandedFAQ(expandedFAQ === id ? null : id);
  };

  return (
    <PageLayout
      title="How can we help you?"
      subtitle="Find answers to common questions or get in touch with our support team."
      showHero={true}
      navigationProps={{ title: "Help Center" }}
      heroProps={{
        showSearch: true,
        searchPlaceholder: "Search for help...",
        onSearch: setSearchQuery
      }}
    >

      <HelpCategories categories={helpCategories} />

      {/* FAQ Section */}
      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">
              {searchQuery ? 'Search Results' : 'Frequently Asked Questions'}
            </h2>
            <p className="text-gray-600 text-lg">
              {searchQuery ? `Found ${filteredFAQs.length} results` : 'Quick answers to common questions'}
            </p>
          </div>

          <div className="space-y-4">
            {filteredFAQs.map((faq) => (
              <div key={faq.id} className="bg-gray-50 rounded-lg overflow-hidden">
                <button
                  onClick={() => toggleFAQ(faq.id)}
                  className="w-full px-6 py-4 text-left flex justify-between items-center hover:bg-gray-100 transition"
                >
                  <span className="text-lg font-semibold text-gray-900">{faq.question}</span>
                  {expandedFAQ === faq.id ? (
                    <ChevronUp className="w-5 h-5 text-gray-500" />
                  ) : (
                    <ChevronDown className="w-5 h-5 text-gray-500" />
                  )}
                </button>
                {expandedFAQ === faq.id && (
                  <div className="px-6 pb-4">
                    <p className="text-gray-600 leading-relaxed">{faq.answer}</p>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </section>

      <ContactSupport />
    </PageLayout>
  );
};

export default HelpCenterPage;
