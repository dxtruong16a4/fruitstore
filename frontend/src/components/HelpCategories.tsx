import React from 'react';

interface HelpCategory {
  title: string;
  icon: string;
  topics: string[];
}

interface HelpCategoriesProps {
  categories: HelpCategory[];
  className?: string;
}

const HelpCategories: React.FC<HelpCategoriesProps> = ({
  categories,
  className = ''
}) => {
  return (
    <section className={`py-16 ${className}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-bold text-gray-900 mb-4">Tìm trợ giúp theo danh mục</h2>
          <p className="text-gray-600 text-lg">Tìm trợ giúp theo danh mục</p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {categories.map((category, index) => (
            <div key={index} className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition">
              <div className="text-4xl mb-4">{category.icon}</div>
              <h3 className="text-xl font-semibold text-gray-900 mb-4">{category.title}</h3>
              <ul className="space-y-2">
                {category.topics.map((topic, topicIndex) => (
                  <li key={topicIndex} className="text-gray-600 hover:text-green-600 cursor-pointer transition">
                    {topic}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default HelpCategories;
