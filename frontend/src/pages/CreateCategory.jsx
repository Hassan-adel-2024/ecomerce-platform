import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateCategory() {
  const [categoryName, setCategoryName] = useState('');
  const [description, setDescription] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

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

      const response = await fetch('http://localhost:8080/category/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ categoryName, description }),
      });

      if (response.ok) {
        setSuccess('Category created successfully!');
        setCategoryName('');
        setDescription('');
        setTimeout(() => {
          navigate('/');
        }, 1500);
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to create category' }));
        setError(errorData.message || 'Failed to create category');
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
            Create New Category
          </h1>
          <p className="text-gray-600 mt-2">Add a new category to organize your products</p>
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
              <label htmlFor="categoryName" className="text-sm font-semibold text-gray-700">
                Category Name <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                id="categoryName"
                value={categoryName}
                onChange={(e) => setCategoryName(e.target.value)}
                placeholder="e.g., Electronics, Clothing, Books"
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
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Describe this category..."
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
                disabled={isLoading}
                className="flex-1 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl text-base font-semibold cursor-pointer transition-all duration-300 shadow-lg shadow-indigo-500/40 hover:shadow-xl hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:translate-y-0 disabled:hover:shadow-lg"
              >
                {isLoading ? 'Creating...' : 'Create Category'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateCategory;

