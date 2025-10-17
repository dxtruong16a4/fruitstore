import React from 'react';

interface FAQ {
  id: number;
  question: string;
  answer: string;
}

interface FAQSectionProps {
  faqs: FAQ[];
  title?: string;
  subtitle?: string;
  className?: string;
}

const FAQSection: React.FC<FAQSectionProps> = ({
  faqs,
  title = "Câu hỏi thường gặp",
  subtitle = "Câu trả lời nhanh cho các câu hỏi thường gặp",
  className = ''
}) => {
  return (
    <section className={`py-16 bg-white ${className}`}>
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">{title}</h2>
          <p className="text-gray-600">{subtitle}</p>
        </div>

        <div className="space-y-6">
          {faqs.map((faq) => (
            <div key={faq.id} className="bg-gray-50 p-6 rounded-lg">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">{faq.question}</h3>
              <p className="text-gray-600">{faq.answer}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default FAQSection;
