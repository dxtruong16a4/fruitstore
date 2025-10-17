import React from 'react';
import { MessageCircle, Phone, Mail } from 'lucide-react';

interface ContactMethod {
  icon: React.ReactNode;
  title: string;
  description: string;
  buttonText: string;
  action: () => void;
}

interface ContactSupportProps {
  methods?: ContactMethod[];
  className?: string;
}

const ContactSupport: React.FC<ContactSupportProps> = ({
  methods,
  className = ''
}) => {
  const defaultMethods: ContactMethod[] = [
    {
      icon: <MessageCircle className="w-8 h-8 text-green-600" />,
      title: "Live Chat",
      description: "Get instant help from our support team",
      buttonText: "Start Chat",
      action: () => console.log("Starting live chat...")
    },
    {
      icon: <Phone className="w-8 h-8 text-green-600" />,
      title: "Phone Support",
      description: "Call us at (555) 123-4567",
      buttonText: "Call Now",
      action: () => console.log("Calling support...")
    },
    {
      icon: <Mail className="w-8 h-8 text-green-600" />,
      title: "Email Support",
      description: "Send us an email anytime",
      buttonText: "Send Email",
      action: () => console.log("Opening email...")
    }
  ];

  const supportMethods = methods || defaultMethods;

  return (
    <section className={`py-16 ${className}`}>
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-bold text-gray-900 mb-4">Still need help?</h2>
          <p className="text-gray-600 text-lg">Our support team is here to assist you</p>
        </div>

        <div className="grid md:grid-cols-3 gap-8">
          {supportMethods.map((method, index) => (
            <div key={index} className="text-center p-6 bg-white rounded-2xl shadow-md">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                {method.icon}
              </div>
              <h3 className="text-xl font-semibold mb-2">{method.title}</h3>
              <p className="text-gray-600 mb-4">{method.description}</p>
              <button 
                onClick={method.action}
                className="bg-green-600 text-white px-6 py-2 rounded-full font-semibold hover:bg-green-700 transition"
              >
                {method.buttonText}
              </button>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default ContactSupport;
