import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Index from './pages/Index';
import CreateProduct from './pages/CreateProduct';
import CreateCategory from './pages/CreateCategory';
import CreateSubCategory from './pages/CreateSubCategory';
import './App.css';

// Protected Route Component
function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" replace />;
}

// Public Route Component (redirects to index if already logged in)
function PublicRoute({ children }) {
  const token = localStorage.getItem('token');
  return !token ? children : <Navigate to="/" replace />;
}

function App() {
  return (
    <Router>
      <Routes>
        <Route 
          path="/login" 
          element={
            <PublicRoute>
              <Login />
            </PublicRoute>
          } 
        />
        <Route 
          path="/" 
          element={
            <ProtectedRoute>
              <Index />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/create-product" 
          element={
            <ProtectedRoute>
              <CreateProduct />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/create-category" 
          element={
            <ProtectedRoute>
              <CreateCategory />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/create-subcategory" 
          element={
            <ProtectedRoute>
              <CreateSubCategory />
            </ProtectedRoute>
          } 
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
