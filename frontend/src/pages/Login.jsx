import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        // Store token if provided
        if (data.token) {
          localStorage.setItem('token', data.token);
        }
        navigate('/');
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Invalid email or password');
      }
    } catch (err) {
      setError('Connection error. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden bg-gradient-to-br from-indigo-500 via-purple-500 to-purple-600 p-5">
      {/* Animated Background Shapes */}
      <div className="absolute inset-0 overflow-hidden z-0">
        <div className="absolute w-[300px] h-[300px] -top-[100px] -left-[100px] rounded-full bg-white/10 backdrop-blur-md animate-float"></div>
        <div className="absolute w-[200px] h-[200px] -bottom-[50px] -right-[50px] rounded-full bg-white/10 backdrop-blur-md animate-float-delayed-1"></div>
        <div className="absolute w-[150px] h-[150px] top-1/2 right-[10%] rounded-full bg-white/10 backdrop-blur-md animate-float-delayed-2"></div>
      </div>
      
      {/* Login Card */}
      <div className="bg-white/95 backdrop-blur-xl rounded-2xl sm:rounded-3xl p-6 sm:p-12 w-full max-w-[440px] shadow-2xl relative z-10 animate-slide-up">
        <div className="text-center mb-8 sm:mb-10">
          <h1 className="text-3xl sm:text-4xl font-bold text-gray-900 mb-2 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
            Welcome Back
          </h1>
          <p className="text-sm sm:text-base text-gray-600">
            Sign in to continue to your account
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5 sm:space-y-6">
          {error && (
            <div className="p-3 bg-red-100 text-red-700 rounded-xl text-sm border-l-4 border-red-500">
              {error}
            </div>
          )}
          
          <div className="space-y-2">
            <label htmlFor="email" className="text-sm font-semibold text-gray-700">
              Email Address
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter your email"
              required
              autoComplete="email"
              className="w-full px-4 py-3 sm:py-3.5 border-2 border-gray-200 rounded-xl text-sm sm:text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
            />
          </div>

          <div className="space-y-2">
            <label htmlFor="password" className="text-sm font-semibold text-gray-700">
              Password
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              required
              autoComplete="current-password"
              className="w-full px-4 py-3 sm:py-3.5 border-2 border-gray-200 rounded-xl text-sm sm:text-base transition-all duration-300 bg-white text-gray-900 placeholder:text-gray-400 focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10"
            />
          </div>

          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 sm:gap-0 text-sm">
            <label className="flex items-center gap-2 text-gray-600 cursor-pointer">
              <input 
                type="checkbox" 
                className="w-[18px] h-[18px] cursor-pointer accent-indigo-600"
              />
              <span>Remember me</span>
            </label>
            <a href="#" className="text-indigo-600 font-medium hover:text-purple-600 hover:underline transition-colors">
              Forgot password?
            </a>
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            className="w-full py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl text-base font-semibold cursor-pointer transition-all duration-300 shadow-lg shadow-indigo-500/40 hover:shadow-xl hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:translate-y-0 disabled:hover:shadow-lg mt-2"
          >
            {isLoading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div className="mt-6 sm:mt-8 text-center text-xs sm:text-sm text-gray-600">
          <p>
            Don't have an account?{' '}
            <a href="/register" className="text-indigo-600 font-semibold hover:text-purple-600 hover:underline transition-colors">
              Sign up
            </a>
          </p>
        </div>
      </div>

      <style>{`
        @keyframes float {
          0%, 100% {
            transform: translate(0, 0) rotate(0deg);
          }
          33% {
            transform: translate(30px, -30px) rotate(120deg);
          }
          66% {
            transform: translate(-20px, 20px) rotate(240deg);
          }
        }
        @keyframes slide-up {
          from {
            opacity: 0;
            transform: translateY(30px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .animate-float {
          animation: float 20s infinite ease-in-out;
        }
        .animate-float-delayed-1 {
          animation: float 20s infinite ease-in-out 5s;
        }
        .animate-float-delayed-2 {
          animation: float 20s infinite ease-in-out 10s;
        }
        .animate-slide-up {
          animation: slide-up 0.6s ease-out;
        }
      `}</style>
    </div>
  );
}

export default Login;
