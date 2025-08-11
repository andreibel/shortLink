import {Route, Routes} from "react-router-dom";
import Navbar from "./components/NavBar";
import ShortenUrlPage from "./components/ShortenUrlPage";
import {Toaster} from "react-hot-toast";
import Footer from "./components/Footer";
import LandingPage from "./components/LandingPage";
import AboutPage from "./components/AboutPage";
import RegisterPage from "./components/RegisterPage";
import LoginPage from "./components/LoginPage";
import DashboardLayout from "./components/Dashboard/DashboardLayout";
import PrivateRoute from "./PrivateRoute";
import ErrorPage from "./components/ErrorPage";

/**
 * AppRouter component defines the main routing structure for the application.
 * It conditionally renders the Navbar and Footer based on the current path,
 * and sets up routes for all main pages including landing, about, authentication,
 * dashboard, shortened URL handling, and error pages.
 *
 * @returns {JSX.Element} The application's router with all defined routes.
 */
const AppRouter = () => {
  // Hide header and footer for shortened URL pages
  const hideHeaderFooter = location.pathname.startsWith("/s");

  return (<>
      {/* Conditionally render Navbar */}
      {!hideHeaderFooter && <Navbar/>}
      {/* Global toast notifications */}
      <Toaster position='bottom-center'/>
      <Routes>
        {/* Landing page route */}
        <Route path="/" element={<LandingPage/>}/>
        {/* About page route */}
        <Route path="/about" element={<AboutPage/>}/>
        {/* Shortened URL redirect page */}
        <Route path="/s/:url" element={<ShortenUrlPage/>}/>
        {/* Registration page (public route) */}
        <Route path="/register" element={<PrivateRoute publicPage={true}><RegisterPage/></PrivateRoute>}/>
        {/* Login page (public route) */}
        <Route path="/login" element={<PrivateRoute publicPage={true}><LoginPage/></PrivateRoute>}/>
        {/* Dashboard page (private route) */}
        <Route path="/dashboard" element={<PrivateRoute publicPage={false}><DashboardLayout/></PrivateRoute>}/>
        {/* Generic error page */}
        <Route path="/error" element={<ErrorPage/>}/>
        {/* Catch-all route for undefined pages */}
        <Route path="*" element={<ErrorPage message="We can't seem to find the page you're looking for"/>}/>
      </Routes>
      {/* Conditionally render Footer */}
      {!hideHeaderFooter && <Footer/>}
    </>);
}

export default AppRouter;

/**
 * SubDomainRouter component handles routing for subdomain-based shortened URLs.
 *
 * @returns {JSX.Element} Router for subdomain URL redirection.
 */
export const SubDomainRouter = () => {
  return (<Routes>
      {/* Shortened URL redirect for subdomain */}
      <Route path="/:url" element={<ShortenUrlPage/>}/>
    </Routes>)
}