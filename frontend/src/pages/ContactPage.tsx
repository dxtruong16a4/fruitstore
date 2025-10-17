import React from 'react';
import { ContactInfo, ContactForm, FAQSection } from '../components';
import { PageLayout } from '../components/layout';

const ContactPage: React.FC = () => {
  const faqs = [
    {
      id: 1,
      question: "How quickly do you deliver?",
      answer: "We offer same-day delivery for orders placed before 2 PM, and next-day delivery for all other orders."
    },
    {
      id: 2,
      question: "Do you offer organic fruits?",
      answer: "Yes! We have a wide selection of certified organic fruits from local farms."
    },
    {
      id: 3,
      question: "What if I'm not satisfied with my order?",
      answer: "We offer a 100% satisfaction guarantee. Contact us within 24 hours and we'll make it right."
    }
  ];

  return (
    <PageLayout
      title="Get in Touch"
      subtitle="We'd love to hear from you. Send us a message and we'll respond as soon as possible."
      showHero={true}
      navigationProps={{ title: "Contact Us" }}
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
