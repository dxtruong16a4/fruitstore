import React from 'react';
import { Heart, Users, Award, Globe } from 'lucide-react';
import { PageLayout } from '../components/layout';

const AboutPage: React.FC = () => {
  return (
    <PageLayout
      title="About FruitStore"
      subtitle="We're passionate about bringing you the freshest, highest-quality fruits from local farms to your doorstep."
      showHero={true}
      navigationProps={{ title: "About Us" }}
    >

      {/* Our Story */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-4xl font-bold text-gray-900 mb-6">Our Story</h2>
              <div className="space-y-4 text-gray-600 leading-relaxed">
                <p>
                  Founded in 2020, FruitStore began with a simple mission: to connect people with the freshest, 
                  most delicious fruits available. What started as a small family business has grown into 
                  a trusted online marketplace serving thousands of satisfied customers.
                </p>
                <p>
                  We work directly with local farmers and suppliers to ensure that every piece of fruit 
                  meets our high standards for quality, freshness, and taste. Our commitment to sustainable 
                  farming practices means you can enjoy your favorite fruits while supporting local communities.
                </p>
                <p>
                  Today, we're proud to offer a wide selection of seasonal fruits, exotic varieties, 
                  and organic options, all delivered fresh to your door with care and attention to detail.
                </p>
              </div>
            </div>
            <div className="relative">
              <img
                src="https://images.pexels.com/photos/1070531/pexels-photo-1070531.jpeg?auto=compress&cs=tinysrgb&w=800"
                alt="Fresh fruits from local farms"
                className="rounded-2xl shadow-xl w-full h-auto"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Our Values */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Our Values</h2>
            <p className="text-gray-600 text-lg">What drives us every day</p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Heart className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Quality First</h3>
              <p className="text-gray-600">We never compromise on the quality and freshness of our fruits.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Users className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Community</h3>
              <p className="text-gray-600">Supporting local farmers and building lasting relationships.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Globe className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Sustainability</h3>
              <p className="text-gray-600">Committed to eco-friendly practices and sustainable farming.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Award className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Excellence</h3>
              <p className="text-gray-600">Striving for excellence in every aspect of our service.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Team Section */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Meet Our Team</h2>
            <p className="text-gray-600 text-lg">The passionate people behind FruitStore</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">JS</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">John Smith</h3>
              <p className="text-green-600 font-medium mb-2">Founder & CEO</p>
              <p className="text-gray-600 text-sm">Passionate about fresh produce and sustainable farming practices.</p>
            </div>

            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">MJ</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Maria Johnson</h3>
              <p className="text-green-600 font-medium mb-2">Head of Operations</p>
              <p className="text-gray-600 text-sm">Ensuring every order is handled with care and attention to detail.</p>
            </div>

            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">DW</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">David Wilson</h3>
              <p className="text-green-600 font-medium mb-2">Quality Assurance</p>
              <p className="text-gray-600 text-sm">Making sure only the best fruits make it to your table.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default AboutPage;
