import React from 'react';
import { Link } from 'react-router-dom';
import { Package, Shield, Clock, Users, ArrowRight } from 'lucide-react';

const Home = () => {
    return (
        <div className="container">
            {/* Hero Section */}
            <section style={{
                minHeight: '80vh',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                textAlign: 'center',
                padding: '4rem 0'
            }}>
                <div className="glass-card animate-in" style={{ padding: '3rem', maxWidth: '900px', width: '100%', border: '1px solid rgba(255,255,255,0.1)' }}>
                    <div style={{
                        background: 'linear-gradient(135deg, var(--primary) 0%, #a855f7 100%)',
                        WebkitBackgroundClip: 'text',
                        WebkitTextFillColor: 'transparent',
                        fontSize: '3.5rem',
                        fontWeight: '800',
                        marginBottom: '1rem',
                        lineHeight: '1.2'
                    }}>
                        Rent Anything, <br /> Anytime, Anywhere.
                    </div>
                    <p style={{ fontSize: '1.25rem', color: 'var(--text-muted)', maxWidth: '600px', margin: '0 auto 2.5rem' }}>
                        Join the community where ownership is optional. Rent high-quality items from verified owners or earn money by listing your own safely.
                    </p>
                    <div style={{ display: 'flex', gap: '1.5rem', justifyContent: 'center', flexWrap: 'wrap' }}>
                        <Link to="/register" className="btn-primary" style={{ padding: '1rem 2.5rem', fontSize: '1.1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                            Get Started <ArrowRight size={20} />
                        </Link>
                        <Link to="/login" className="btn-outline" style={{ padding: '1rem 2.5rem', fontSize: '1.1rem' }}>
                            Sign In
                        </Link>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section style={{ padding: '4rem 0' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '3rem', fontSize: '2.5rem' }}>Why Choose RentIt?</h2>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '2rem' }}>
                    <FeatureCard
                        icon={<Shield size={32} color="var(--primary)" />}
                        title="Secure & Safe"
                        description="Every rental is protected. We verify all users to ensure a safe community for everyone."
                    />
                    <FeatureCard
                        icon={<Package size={32} color="#ec4899" />}
                        title="Wide Variety"
                        description="From cameras to camping gear, find exactly what you need for your next adventure."
                    />
                    <FeatureCard
                        icon={<Clock size={32} color="#eab308" />}
                        title="Flexible Rentals"
                        description="Rent for a day, a week, or a month. Pay only for the time you need the item."
                    />
                    <FeatureCard
                        icon={<Users size={32} color="#22c55e" />}
                        title="Community Driven"
                        description="Connect with locals. Save money by renting instead of buying, or earn by sharing."
                    />
                </div>
            </section>
        </div>
    );
};

const FeatureCard = ({ icon, title, description }) => (
    <div className="glass-card animate-in" style={{ padding: '2rem', textAlign: 'left', transition: 'transform 0.2s', cursor: 'default' }}>
        <div style={{
            width: '64px',
            height: '64px',
            borderRadius: '16px',
            background: 'var(--glass)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            marginBottom: '1.5rem'
        }}>
            {icon}
        </div>
        <h3 style={{ marginBottom: '1rem', fontSize: '1.25rem' }}>{title}</h3>
        <p style={{ color: 'var(--text-muted)', lineHeight: '1.6' }}>{description}</p>
    </div>
);

export default Home;
