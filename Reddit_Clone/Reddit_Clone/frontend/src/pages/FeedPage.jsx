import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiRequest, postJson } from '../api/client.js';
import PostCard from '../components/PostCard.jsx';
import SortTabs from '../components/SortTabs.jsx';
import Status from '../components/Status.jsx';
import { useAuth } from '../state/AuthContext.jsx';

/**
 * Displays the home feed and community browser.
 */
export default function FeedPage() {
  const [sort, setSort] = useState('latest');
  const [posts, setPosts] = useState([]);
  const [communities, setCommunities] = useState([]);
  const [communityForm, setCommunityForm] = useState({ name: '', description: '' });
  const [status, setStatus] = useState({ loading: true, error: '' });
  const { isAuthenticated } = useAuth();

  /**
   * Loads posts and communities from the API.
   */
  const load = useCallback(async function load() {
    setStatus({ loading: true, error: '' });
    try {
      const [nextPosts, nextCommunities] = await Promise.all([
        apiRequest(`/posts?sort=${sort}`),
        apiRequest('/communities')
      ]);
      setPosts(nextPosts);
      setCommunities(nextCommunities);
      setStatus({ loading: false, error: '' });
    } catch (err) {
      setStatus({ loading: false, error: err.message });
    }
  }, [sort]);

  /**
   * Creates a community then refreshes the sidebar.
   */
  async function createCommunity(event) {
    event.preventDefault();
    await postJson('/communities', communityForm);
    setCommunityForm({ name: '', description: '' });
    await load();
  }

  useEffect(() => {
    load();
  }, [load]);

  return (
    <div className="grid gap-6 lg:grid-cols-[1fr_320px]">
      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Home feed</h1>
          <SortTabs sort={sort} onChange={setSort} />
        </div>
        {status.loading && <Status>Loading posts...</Status>}
        {status.error && <Status type="error">{status.error}</Status>}
        {!status.loading && !status.error && posts.length === 0 && <Status>No posts yet. Create a community and start the first thread.</Status>}
        {posts.map((post) => <PostCard key={post.id} post={post} onVote={load} />)}
      </section>
      <aside className="space-y-4">
        <div className="rounded-md border border-slate-200 bg-white p-4">
          <h2 className="font-bold">Communities</h2>
          <div className="mt-3 space-y-2">
            {communities.map((community) => (
              <Link key={community.id} className="block rounded-md border border-slate-100 px-3 py-2 hover:bg-slate-50" to={`/c/${community.slug}`}>
                <strong>c/{community.name}</strong>
                {community.description && <p className="text-sm text-slate-500">{community.description}</p>}
              </Link>
            ))}
            {communities.length === 0 && <p className="text-sm text-slate-500">No communities yet.</p>}
          </div>
        </div>
        <div className="rounded-md border border-slate-200 bg-white p-4">
          <h2 className="font-bold">Create community</h2>
          {isAuthenticated ? (
            <form className="mt-3 space-y-3" onSubmit={createCommunity}>
              <input className="w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Community name" value={communityForm.name} onChange={(e) => setCommunityForm({ ...communityForm, name: e.target.value })} required />
              <textarea className="min-h-24 w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Description" value={communityForm.description} onChange={(e) => setCommunityForm({ ...communityForm, description: e.target.value })} />
              <button className="w-full rounded-md bg-ink px-3 py-2 font-semibold text-white">Create</button>
            </form>
          ) : (
            <p className="mt-3 text-sm text-slate-500">Log in to create communities and posts.</p>
          )}
        </div>
      </aside>
    </div>
  );
}
