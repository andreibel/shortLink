import React from "react";
import {FaChartLine, FaEdit, FaLink, FaShareAlt} from "react-icons/fa";

/**
 * AboutPage component displays information about the ShortLink service,
 * including its features and benefits.
 *
 * @returns {JSX.Element} The About page content.
 */
const AboutPage = () => {
  return (
    <div className="lg:px-14 sm:px-8 px-5 min-h-[calc(100vh-64px)] pt-2">
      <div className="bg-white w-full sm:py-10 py-8">
        <h1 className="sm:text-4xl text-slate-800 text-3xl font-bold italic mb-3">
          About ShortLink
        </h1>
        <p className="text-gray-700 text-sm mb-8 xl:w-[60%] lg:w-[70%] sm:w-[80%] w-full">
          ShortLink simplifies URL shortening for efficient sharing. Easily generate, manage, and track your shortened
          links.
          <br/><br/>
          Whether you’re a marketer, developer, or everyday user, ShortLink provides a seamless experience for
          transforming long, unwieldy URLs into concise, shareable links. Our platform is designed to save you time,
          improve your workflow, and help you get the most out of your online presence. Discover how ShortLink can make
          your digital life easier and more productive.
        </p>
        <div className="space-y-5 xl:w-[60%] lg:w-[70%] sm:w-[80%] w-full">
          <div className="flex items-start">
            <FaLink className="text-blue-500 text-3xl mr-4"/>
            <div>
              <h2 className="sm:text-2xl font-bold text-slate-800">
                Simple URL Shortening
              </h2>
              <p className="text-gray-600">
                Experience the ease of creating short, memorable URLs in just a few clicks. Our intuitive interface and
                quick setup process ensure you can start shortening URLs without any hassle.
                <br/>
                No registration required—just paste your link and get a shortened version instantly. Perfect for sharing
                on social media, emails, or anywhere you need a clean, concise link.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <FaShareAlt className="text-green-500 text-3xl mr-4"/>
            <div>
              <h2 className="sm:text-2xl font-bold text-slate-800">
                Powerful Analytics
              </h2>
              <p className="text-gray-600">
                Gain insights into your link performance with our comprehensive analytics dashboard. Track clicks,
                geographical data, and referral sources to optimize your marketing strategies.
                <br/>
                Visualize your audience engagement and discover which channels drive the most traffic. Our analytics
                tools help you make informed decisions and maximize your reach.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <FaEdit className="text-purple-500 text-3xl mr-4"/>
            <div>
              <h2 className="sm:text-2xl font-bold text-slate-800">
                Enhanced Security
              </h2>
              <p className="text-gray-600">
                Rest assured with our robust security measures. All shortened URLs are protected with advanced
                encryption, ensuring your data remains safe and secure.
                <br/>
                We prioritize your privacy and safety, implementing regular security audits and updates to keep your
                information protected from threats.
              </p>
            </div>
          </div>
          <div className="flex items-start">
            <FaChartLine className="text-red-500 text-3xl mr-4"/>
            <div>
              <h2 className="sm:text-2xl font-bold text-slate-800">
                Fast and Reliable
              </h2>
              <p className="text-gray-600">
                Enjoy lightning-fast redirects and high uptime with our reliable infrastructure. Your shortened URLs
                will always be available and responsive, ensuring a seamless experience for your users.
                <br/>
                Our global network ensures minimal latency and maximum reliability, so your links work perfectly every
                time, no matter where your audience is located.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AboutPage;