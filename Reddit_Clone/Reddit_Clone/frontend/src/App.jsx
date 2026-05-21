import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout.jsx';
import FeedPage from './pages/FeedPage.jsx';
import AuthPage from './pages/AuthPage.jsx';
import CommunityPage from './pages/CommunityPage.jsx';
import PostDetailPage from './pages/PostDetailPage.jsx';
import CreatePostPage from './pages/CreatePostPage.jsx';
import { useAuth } from './state/AuthContext.jsx';

/**
 * Declares the full client-side route map.
 */
export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<FeedPage />} />
        <Route path="/auth" element={<AuthPage />} />
        <Route path="/c/:slug" element={<CommunityPage />} />
        <Route path="/posts/:id" element={<PostDetailPage />} />
        <Route path="/create" element={<Protected><CreatePostPage /></Protected>} />
      </Route>
    </Routes>
  );
}

/**
 * Redirects anonymous users away from authenticated-only screens.
 */
function Protected({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/auth" replace />;
}
