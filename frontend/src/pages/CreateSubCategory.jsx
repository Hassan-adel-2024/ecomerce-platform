import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateSubCategory() {
  const [formData, setFormData] = useState({
    categoryId: '',
    subCategoryName: '',
    description: '',
  });
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch('http://localhost:8080/category/all', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setCategories(data);
      }
    } catch (err) {
      console.error('Error fetching categories:', err);
    } finally {
      setIsLoadingCategories(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setIsLoading(true);

    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const subCategoryData = {
        ...formData,
        categoryId: parseInt(formData.categoryId),
      };

      const response = await fetch('http://localhost:8080/subcategory/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(subCategoryData),
      });

      if (response.ok) {
        setSuccess('SubCategory created successfully!');
        setFormData({
          categoryId: '',
          subCategoryName: '',
          description: '',
        });
        setTimeout(() => {
          navigate('/');
        }, 1500);
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to create subcategory' }));
        setError(errorData.message || 'Failed to create subcategory');
      }
    } catch (err) {
      setError('Connection error. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-200 py-8 px-4">
      <div className="max-w-2xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate('/')}
            className="mb-4 text-indigo-600 hover:text-purple-600 font-semibold transition-colors flex items-center gap-2"
          >
            ← Back to Dashboard
          </button>
          <h1 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
            Create New SubCategory
          </h1>
          <p className="text-gray-600 mt-2">Add a new subcategory under an existing category</p>
        </div>

        {/* Form Card */}
        <div className="bg-white/95 backdrop-blur-xl rounded-2xl sm:rounded-3xl p-6 sm:p-12 shadow-2xl">
          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <div className="p-4 bg-red-100 text-red-700 rounded-xl text-sm border-l-4 border-red-500">
                {error}
              </div>
            )}
            
            {success && (
              <div className="p-4 bg-green-100 text-green-700 rounded-xl text-sm border-l-4 border-green-500">
                {success}
              </div>
            )}

            <div className="space-y-2">
              <label htmlFor="categoryId" className="text-sm font-semibold text-gray-700">
                Parent Category <span className="text-red-500">*</span>
              </label>
              {isLoadingCategories ? (
                <div className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl bg-gray-50 animate-pulse">
                  Loading categories...
                </div>
              ) : (
                <select
                  id="categoryId"
                  name="categoryId"
                  value={formData.categoryId}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                >
                  <option value="">Select a parent category</option>
                  {categories.map((category) => (
                    <option key={category.categoryId} value={category.categoryId}>
                      {category.categoryName}
                    </option>
                  ))}
                </select>
              )}
              {categories.length === 0 && !isLoadingCategories && (
                <p className="text-sm text-gray-500">
                  No categories found. <a href="/create-category" className="text-indigo-600 hover:underline">Create one first</a>
                </p>
              )}
            </div>

            <div className="space-y-2">
              <label htmlFor="subCategoryName" className="text-sm font-semibold text-gray-700">
                SubCategory Name <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                id="subCategoryName"
                name="subCategoryName"
                value={formData.subCategoryName}
                onChange={handleChange}
                placeholder="e.g., Smartphones, Laptops, Tablets"
                required
                className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
              />
            </div>

            <div className="space-y-2">
              <label htmlFor="description" className="text-sm font-semibold text-gray-700">
                Description
              </label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Describe this subcategory..."
                rows="4"
                className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10 resize-none"
              />
            </div>

            <div className="flex flex-col sm:flex-row gap-4 pt-4">
              <button
                type="button"
                onClick={() => navigate('/')}
                className="flex-1 py-4 bg-gray-200 text-gray-700 rounded-xl text-base font-semibold cursor-pointer transition-all duration-300 hover:bg-gray-300"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={isLoading || isLoadingCategories}
                className="flex-1 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl text-base font-semibold cursor-pointer transition-all duration-300 shadow-lg shadow-indigo-500/40 hover:shadow-xl hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:translate-y-0 disabled:hover:shadow-lg"
              >
                {isLoading ? 'Creating...' : 'Create SubCategory'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateSubCategory;

