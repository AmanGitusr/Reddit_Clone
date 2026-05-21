import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { postJson, setToken, getToken } from '../api/client.js';

const AuthContext = createContext(null);

/**
 * Stores the signed-in user and exposes login, signup, and logout actions.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('redditCloneUser');
    return raw ? JSON.parse(raw) : null;
  });

  /**
   * Saves auth response details in local state and browser storage.
   */
  const persistAuth = useCallback(function persistAuth(payload) {
    setToken(payload.token);
    const nextUser = { userId: payload.userId, username: payload.username, email: payload.email };
    localStorage.setItem('redditCloneUser', JSON.stringify(nextUser));
    setUser(nextUser);
  }, []);

  /**
   * Registers a user then stores the returned JWT.
   */
  const signup = useCallback(async function signup(form) {
    persistAuth(await postJson('/auth/signup', form));
  }, [persistAuth]);

  /**
   * Logs in a user then stores the returned JWT.
   */
  const login = useCallback(async function login(form) {
    persistAuth(await postJson('/auth/login', form));
  }, [persistAuth]);

  /**
   * Clears all local authentication state.
   */
  const logout = useCallback(function logout() {
    setToken(null);
    localStorage.removeItem('redditCloneUser');
    setUser(null);
  }, []);

  const value = useMemo(() => ({
    user,
    token: getToken(),
    isAuthenticated: Boolean(user && getToken()),
    signup,
    login,
    logout
  }), [login, logout, signup, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

/**
 * Returns the auth context for components that need identity or auth actions.
 */
export function useAuth() {
  return useContext(AuthContext);
}
