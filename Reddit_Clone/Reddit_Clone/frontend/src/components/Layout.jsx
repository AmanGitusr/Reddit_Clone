import { Link, NavLink, Outlet } from 'react-router-dom';
import { LogOut, Plus, Search, UserCircle } from 'lucide-react';
import { useAuth } from '../state/AuthContext.jsx';

/**
 * Provides the shared shell with navigation and routed page content.
 */
export default function Layout() {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <div className="min-h-screen">
      <header className="sticky top-0 z-10 border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-6xl items-center gap-3 px-4 py-3">
          <Link to="/" className="flex items-center gap-2 text-xl font-bold text-ember">
            <span className="grid h-9 w-9 place-items-center rounded-full bg-ember text-white">r</span>
            <span>Clone</span>
          </Link>
          <div className="hidden flex-1 items-center rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-slate-500 md:flex">
            <Search className="mr-2 h-4 w-4" />
            Search communities and posts by browsing the feed
          </div>
          {isAuthenticated ? (
            <>
              <NavLink className="inline-flex items-center gap-2 rounded-md bg-ember px-3 py-2 text-sm font-semibold text-white" to="/create">
                <Plus className="h-4 w-4" /> Post
              </NavLink>
              <span className="hidden items-center gap-2 text-sm text-slate-600 sm:inline-flex">
                <UserCircle className="h-4 w-4" /> {user.username}
              </span>
              <button className="rounded-md border border-slate-200 p-2 text-slate-600 hover:bg-slate-50" onClick={logout} title="Log out">
                <LogOut className="h-4 w-4" />
              </button>
            </>
          ) : (
            <NavLink className="rounded-md bg-ink px-3 py-2 text-sm font-semibold text-white" to="/auth">
              Log in
            </NavLink>
          )}
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-4 py-6">
        <Outlet />
      </main>
    </div>
  );
}
