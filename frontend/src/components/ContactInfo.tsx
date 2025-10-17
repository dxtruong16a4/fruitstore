import React from 'react';
import { Mail, Phone, MapPin, Clock } from 'lucide-react';

interface ContactInfoProps {
  className?: string;
}

const ContactInfo: React.FC<ContactInfoProps> = ({ className = '' }) => {
  return (
    <div className={className}>
      <h2 className="text-3xl font-bold text-gray-900 mb-8">Contact Information</h2>
      
      <div className="space-y-6">
        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Mail className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Email</h3>
            <p className="text-gray-600">hello@fruitstore.com</p>
            <p className="text-gray-600">support@fruitstore.com</p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Phone className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Phone</h3>
            <p className="text-gray-600">+1 (555) 123-4567</p>
            <p className="text-gray-600">Mon-Fri 9AM-6PM EST</p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <MapPin className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Address</h3>
            <p className="text-gray-600">
              123 Fresh Street<br />
              Organic Valley, CA 90210<br />
              United States
            </p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Clock className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Business Hours</h3>
            <div className="text-gray-600 space-y-1">
              <p>Monday - Friday: 9:00 AM - 6:00 PM</p>
              <p>Saturday: 10:00 AM - 4:00 PM</p>
              <p>Sunday: Closed</p>
            </div>
          </div>
        </div>
      </div>

      <div className="mt-8 p-6 bg-green-50 rounded-2xl">
        <h3 className="text-lg font-semibold text-gray-900 mb-2">Need Immediate Help?</h3>
        <p className="text-gray-600 mb-4">
          For urgent matters, please call our customer service line. 
          We're here to help you with any questions or concerns.
        </p>
        <button className="bg-green-600 text-white px-6 py-2 rounded-full font-semibold hover:bg-green-700 transition">
          Call Now
        </button>
      </div>
    </div>
  );
};

export default ContactInfo;
