import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateProduct() {
  const [formData, setFormData] = useState({
    subCategoryId: '',
    productName: '',
    description: '',
    brand: '',
    price: '',
    sku: '',
    quantity: '',
    imageUrl: '',
  });
  const [subCategories, setSubCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingSubCategories, setIsLoadingSubCategories] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchSubCategories();
  }, []);

  const fetchSubCategories = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch('http://localhost:8080/subcategory/all', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setSubCategories(data);
      }
    } catch (err) {
      console.error('Error fetching subcategories:', err);
    } finally {
      setIsLoadingSubCategories(false);
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

      // Convert string values to appropriate types
      const productData = {
        ...formData,
        subCategoryId: parseInt(formData.subCategoryId),
        price: parseFloat(formData.price),
        quantity: parseInt(formData.quantity),
      };

      const response = await fetch('http://localhost:8080/products/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(productData),
      });

      if (response.ok) {
        setSuccess('Product created successfully!');
        // Reset form
        setFormData({
          subCategoryId: '',
          productName: '',
          description: '',
          brand: '',
          price: '',
          sku: '',
          quantity: '',
          imageUrl: '',
        });
        setTimeout(() => {
          navigate('/');
        }, 1500);
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to create product' }));
        setError(errorData.message || 'Failed to create product');
      }
    } catch (err) {
      setError('Connection error. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-200 py-8 px-4">
      <div className="max-w-3xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate('/')}
            className="mb-4 text-indigo-600 hover:text-purple-600 font-semibold transition-colors flex items-center gap-2"
          >
            ← Back to Dashboard
          </button>
          <h1 className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
            Create New Product
          </h1>
          <p className="text-gray-600 mt-2">Add a new product to your catalog</p>
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

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2 md:col-span-2">
                <label htmlFor="subCategoryId" className="text-sm font-semibold text-gray-700">
                  SubCategory <span className="text-red-500">*</span>
                </label>
                {isLoadingSubCategories ? (
                  <div className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl bg-gray-50 animate-pulse">
                    Loading subcategories...
                  </div>
                ) : (
                  <select
                    id="subCategoryId"
                    name="subCategoryId"
                    value={formData.subCategoryId}
                    onChange={handleChange}
                    required
                    className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                  >
                    <option value="">Select a subcategory</option>
                    {subCategories.map((subCategory) => (
                      <option key={subCategory.subCategoryId} value={subCategory.subCategoryId}>
                        {subCategory.subCategoryName} {subCategory.categoryName && `(${subCategory.categoryName})`}
                      </option>
                    ))}
                  </select>
                )}
                {subCategories.length === 0 && !isLoadingSubCategories && (
                  <p className="text-sm text-gray-500">
                    No subcategories found. <a href="/create-subcategory" className="text-indigo-600 hover:underline">Create one first</a>
                  </p>
                )}
              </div>

              <div className="space-y-2 md:col-span-2">
                <label htmlFor="productName" className="text-sm font-semibold text-gray-700">
                  Product Name <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  id="productName"
                  name="productName"
                  value={formData.productName}
                  onChange={handleChange}
                  placeholder="e.g., iPhone 15 Pro"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>

              <div className="space-y-2 md:col-span-2">
                <label htmlFor="description" className="text-sm font-semibold text-gray-700">
                  Description <span className="text-red-500">*</span>
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Describe the product..."
                  rows="4"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10 resize-none"
                />
              </div>

              <div className="space-y-2">
                <label htmlFor="brand" className="text-sm font-semibold text-gray-700">
                  Brand <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  id="brand"
                  name="brand"
                  value={formData.brand}
                  onChange={handleChange}
                  placeholder="e.g., Apple"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>

              <div className="space-y-2">
                <label htmlFor="sku" className="text-sm font-semibold text-gray-700">
                  SKU <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  id="sku"
                  name="sku"
                  value={formData.sku}
                  onChange={handleChange}
                  placeholder="e.g., IPH15PRO-256"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>

              <div className="space-y-2">
                <label htmlFor="price" className="text-sm font-semibold text-gray-700">
                  Price <span className="text-red-500">*</span>
                </label>
                <input
                  type="number"
                  id="price"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="0.00"
                  step="0.01"
                  min="0.01"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>

              <div className="space-y-2">
                <label htmlFor="quantity" className="text-sm font-semibold text-gray-700">
                  Quantity <span className="text-red-500">*</span>
                </label>
                <input
                  type="number"
                  id="quantity"
                  name="quantity"
                  value={formData.quantity}
                  onChange={handleChange}
                  placeholder="0"
                  min="0"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>

              <div className="space-y-2 md:col-span-2">
                <label htmlFor="imageUrl" className="text-sm font-semibold text-gray-700">
                  Image URL <span className="text-red-500">*</span>
                </label>
                <input
                  type="url"
                  id="imageUrl"
                  name="imageUrl"
                  value={formData.imageUrl}
                  onChange={handleChange}
                  placeholder="https://example.com/image.jpg"
                  required
                  className="w-full px-4 py-3.5 border-2 border-gray-200 rounded-xl text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
                />
              </div>
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
                disabled={isLoading || isLoadingSubCategories}
                className="flex-1 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl text-base font-semibold cursor-pointer transition-all duration-300 shadow-lg shadow-indigo-500/40 hover:shadow-xl hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:translate-y-0 disabled:hover:shadow-lg"
              >
                {isLoading ? 'Creating...' : 'Create Product'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateProduct;

