import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Index() {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    
    if (!token) {
      navigate('/login');
      return;
    }

    // Fetch user data
    fetchUserData(token);
  }, [navigate]);

  const fetchUserData = async (token) => {
    try {
      // TODO: Replace with your actual API endpoint
      const response = await fetch('http://localhost:8080/api/user/profile', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
      } else if (response.status === 401) {
        localStorage.removeItem('token');
        navigate('/login');
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-200">
        <div className="w-12 h-12 border-4 border-gray-200 border-t-indigo-600 rounded-full animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-200">
      {/* Navbar */}
      <nav className="bg-white/95 backdrop-blur-xl shadow-lg sticky top-0 z-50 border-b border-black/5">
        <div className="max-w-7xl mx-auto px-5 sm:px-8 py-4 sm:py-5 flex flex-col sm:flex-row justify-between items-center gap-4">
          <div>
            <h1 className="text-xl sm:text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
              E-Commerce Platform
            </h1>
          </div>
          <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 sm:gap-5 w-full sm:w-auto">
            {user && (
              <span className="text-sm sm:text-base font-semibold text-gray-700 px-4 py-2 bg-gray-50 rounded-lg text-center">
                {user.firstName || user.email || 'User'}
              </span>
            )}
            <button 
              onClick={handleLogout}
              className="px-5 py-2.5 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5 w-full sm:w-auto"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-5 sm:px-8 py-6 sm:py-10">
        {/* Welcome Section */}
        <div className="mb-8 sm:mb-10 animate-fade-in">
          <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-2xl p-6 sm:p-10 text-white shadow-xl shadow-indigo-500/30">
            <h2 className="text-2xl sm:text-4xl font-bold mb-2 sm:mb-3">
              Welcome Back! 👋
            </h2>
            <p className="text-base sm:text-lg opacity-95">
              {user 
                ? `Hello ${user.firstName || user.email}, you're successfully logged in.`
                : 'You are successfully logged in.'
              }
            </p>
          </div>
        </div>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5 sm:gap-6 mb-8 sm:mb-10 animate-fade-in-delayed">
          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">📦</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Create Product</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">Add a new product to your catalog</p>
            <button 
              onClick={() => navigate('/create-product')}
              className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5"
            >
              Create Product
            </button>
          </div>

          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">🏷️</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Create Category</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">Add a new product category</p>
            <button 
              onClick={() => navigate('/create-category')}
              className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5"
            >
              Create Category
            </button>
          </div>

          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">📋</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Create SubCategory</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">Add a subcategory under a category</p>
            <button 
              onClick={() => navigate('/create-subcategory')}
              className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5"
            >
              Create SubCategory
            </button>
          </div>

          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">🛒</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Orders</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">Track and manage orders</p>
            <button className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5">
              View Orders
            </button>
          </div>

          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">👥</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Customers</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">Manage customer accounts</p>
            <button className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5">
              View Customers
            </button>
          </div>

          <div className="bg-white rounded-2xl p-6 sm:p-8 shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
            <div className="text-4xl sm:text-5xl mb-3 sm:mb-4">📊</div>
            <h3 className="text-lg sm:text-xl font-bold text-gray-900 mb-2">Analytics</h3>
            <p className="text-sm text-gray-600 mb-5 sm:mb-6">View sales and reports</p>
            <button className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg text-sm font-semibold cursor-pointer transition-all duration-300 shadow-md shadow-indigo-500/30 hover:shadow-lg hover:shadow-indigo-500/40 hover:-translate-y-0.5">
              View Analytics
            </button>
          </div>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-5 animate-fade-in-delayed-2">
          <div className="bg-white rounded-2xl p-5 sm:p-7 text-center shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-xl">
            <div className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">
              0
            </div>
            <div className="text-xs sm:text-sm text-gray-600 font-medium">Total Orders</div>
          </div>
          <div className="bg-white rounded-2xl p-5 sm:p-7 text-center shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-xl">
            <div className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">
              0
            </div>
            <div className="text-xs sm:text-sm text-gray-600 font-medium">Total Revenue</div>
          </div>
          <div className="bg-white rounded-2xl p-5 sm:p-7 text-center shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-xl">
            <div className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">
              0
            </div>
            <div className="text-xs sm:text-sm text-gray-600 font-medium">Active Products</div>
          </div>
          <div className="bg-white rounded-2xl p-5 sm:p-7 text-center shadow-lg border border-black/5 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-xl">
            <div className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2">
              0
            </div>
            <div className="text-xs sm:text-sm text-gray-600 font-medium">Customers</div>
          </div>
        </div>
      </main>

      <style>{`
        @keyframes fade-in {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .animate-fade-in {
          animation: fade-in 0.6s ease-out;
        }
        .animate-fade-in-delayed {
          animation: fade-in 0.8s ease-out;
        }
        .animate-fade-in-delayed-2 {
          animation: fade-in 1s ease-out;
        }
      `}</style>
    </div>
  );
}

export default Index;
