import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { apiRequest } from '../api/client.js';
import PostCard from '../components/PostCard.jsx';
import SortTabs from '../components/SortTabs.jsx';
import Status from '../components/Status.jsx';
import { useAuth } from '../state/AuthContext.jsx';

/**
 * Displays one community and its sorted posts.
 */
export default function CommunityPage() {
  const { slug } = useParams();
  const [sort, setSort] = useState('latest');
  const [community, setCommunity] = useState(null);
  const [posts, setPosts] = useState([]);
  const [status, setStatus] = useState({ loading: true, error: '' });
  const { isAuthenticated } = useAuth();

  /**
   * Loads community metadata and feed data.
   */
  const load = useCallback(async function load() {
    setStatus({ loading: true, error: '' });
    try {
      const [nextCommunity, nextPosts] = await Promise.all([
        apiRequest(`/communities/${slug}`),
        apiRequest(`/communities/${slug}/posts?sort=${sort}`)
      ]);
      setCommunity(nextCommunity);
      setPosts(nextPosts);
      setStatus({ loading: false, error: '' });
    } catch (err) {
      setStatus({ loading: false, error: err.message });
    }
  }, [slug, sort]);

  useEffect(() => {
    load();
  }, [load]);

  if (status.loading) return <Status>Loading community...</Status>;
  if (status.error) return <Status type="error">{status.error}</Status>;

  return (
    <section className="space-y-4">
      <div className="rounded-md border border-slate-200 bg-white p-5">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="text-2xl font-bold">c/{community.name}</h1>
            {community.description && <p className="mt-1 text-slate-600">{community.description}</p>}
          </div>
          {isAuthenticated && <Link className="rounded-md bg-ember px-4 py-2 font-semibold text-white" to="/create">Create post</Link>}
        </div>
      </div>
      <SortTabs sort={sort} onChange={setSort} />
      {posts.length === 0 && <Status>No posts in this community yet.</Status>}
      {posts.map((post) => <PostCard key={post.id} post={post} onVote={load} />)}
    </section>
  );
}
