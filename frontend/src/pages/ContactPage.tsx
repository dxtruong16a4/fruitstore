import React from 'react';
import { ContactInfo, ContactForm, FAQSection } from '../components';
import { PageLayout } from '../components/layout';

const ContactPage: React.FC = () => {
  const faqs = [
    {
      id: 1,
      question: "Bạn giao hàng nhanh như thế nào?",
      answer: "Chúng tôi cung cấp giao hàng ngay hôm đó cho đơn hàng đặt trước 2 PM, và giao hàng ngày mai cho tất cả các đơn hàng khác."
    },
    {
      id: 2,
      question: "Bạn có cung cấp trái cây hữu cơ không?",
      answer: "Có! Chúng tôi có một số loại trái cây hữu cơ từ nông trại địa phương."
    },
    {
      id: 3,
      question: "Tôi không hài lòng với đơn hàng của mình, tôi có thể đổi trả không?",
      answer: "Chúng tôi cung cấp đảm bảo 100% sự hài lòng. Liên hệ chúng tôi trong vòng 24 giờ và chúng tôi sẽ giải quyết nó."
    }
  ];

  return (
    <PageLayout
      title="Liên hệ chúng tôi"
      subtitle="Chúng tôi rất mong nhận được phản hồi từ bạn. Gửi cho chúng tôi một tin nhắn và chúng tôi sẽ trả lời sớm nhất có thể."
      showHero={true}
    >

      {/* Contact Info & Form */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12">
            <ContactInfo />
            <ContactForm />
          </div>
        </div>
      </section>

      <FAQSection faqs={faqs} />
    </PageLayout>
  );
};

export default ContactPage;
