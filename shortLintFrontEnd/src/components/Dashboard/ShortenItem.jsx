import dayjs from 'dayjs';
import React, { useState } from 'react';
import { FaExternalLinkAlt, FaRegCalendarAlt } from 'react-icons/fa';
import { IoCopy } from 'react-icons/io5';
import { LiaCheckSolid } from 'react-icons/lia';
import { MdAnalytics, MdOutlineAdsClick } from 'react-icons/md';
import { Link, useNavigate } from 'react-router-dom';
import { useStoreContext } from '../../contextApi/ContextApi';
import { Hourglass } from 'react-loader-spinner';
import Graph from './Graph';
import { useFetchAnalyticsData } from '../../hooks/useQuery.js';
import toast from "react-hot-toast";

const ShortenItem = ({ originalUrl, shortUrl, clickCount, createdDate }) => {
  const { token } = useStoreContext();
  const navigate = useNavigate();
  const [isCopied, setIsCopied] = useState(false);
  const [analyticToggle, setAnalyticToggle] = useState(false);
  const [selectedUrl, setSelectedUrl] = useState("");

  const subDomain = import.meta.env.VITE_REACT_FRONT_END_URL.replace(/^https?:\/\//, "");
  const fullShortUrl = `${import.meta.env.VITE_REACT_FRONT_END_URL}/s/${shortUrl}`;

  // Fetch analytics using custom hook
  const {
    data: analyticsData = [],
    isLoading: loader,
    isError,
  } = useFetchAnalyticsData({
    shortUrl: selectedUrl,
    token,
    enabled: !!selectedUrl,
    onError: () => navigate("/error"),
  });

  const handleCopyClick = async () => {
    try {
      await navigator.clipboard.writeText(fullShortUrl);
      setIsCopied(true);
      toast.success("Short URL created!");
      setTimeout(() => setIsCopied(false), 3000); // Reset after 3 seconds
    } catch (err) {
      console.error("Clipboard copy failed", err);
    }
  };

  const handleAnalyticsClick = () => {
    if (!analyticToggle) {
      setSelectedUrl(shortUrl);
    } else {
      setSelectedUrl("");
    }
    setAnalyticToggle(!analyticToggle);
  };

  return (
    <div className="bg-slate-100 shadow-lg border border-dotted border-slate-500 px-6 sm:py-1 py-3 rounded-md transition-all duration-100">
      <div className="flex sm:flex-row flex-col sm:justify-between w-full sm:gap-0 gap-5 py-5">
        <div className="flex-1 sm:space-y-1 max-w-full overflow-x-auto overflow-y-hidden">
          <div className="text-slate-900 pb-1 sm:pb-0 flex items-center gap-2">
            <Link
              target="_blank"
              className="text-[17px] font-montserrat font-[600] text-linkColor"
              to={`/s/${shortUrl}`}
            >
              {subDomain + "/s/" + shortUrl}
            </Link>
            <FaExternalLinkAlt className="text-linkColor" />
          </div>

          <div className="flex items-center gap-1">
            <h3 className="text-slate-700 font-[400] text-[17px]">
              {originalUrl}
            </h3>
          </div>

          <div className="flex items-center gap-8 pt-6">
            <div className="flex gap-1 items-center font-semibold text-green-800">
              <span><MdOutlineAdsClick className="text-[22px] me-1" /></span>
              <span className="text-[16px]">{clickCount}</span>
              <span className="text-[15px]">
                {clickCount === 0 || clickCount === 1 ? "Click" : "Clicks"}
              </span>
            </div>

            <div className="flex items-center gap-2 font-semibold text-lg text-slate-800">
              <span><FaRegCalendarAlt /></span>
              <span className="text-[17px]">
                {dayjs(createdDate).format("MMM DD, YYYY")}
              </span>
            </div>
          </div>
        </div>

        <div className="flex flex-1 sm:justify-end items-center gap-4">
          <div
            onClick={handleCopyClick}
            className="flex cursor-pointer gap-1 items-center bg-btnColor py-2 font-semibold shadow-md shadow-slate-500 px-6 rounded-md text-white"
          >
            <button className="">{isCopied ? "Copied" : "Copy"}</button>
            {isCopied ? (
              <LiaCheckSolid className="text-md" />
            ) : (
              <IoCopy className="text-md" />
            )}
          </div>

          <div
            onClick={handleAnalyticsClick}
            className="flex cursor-pointer gap-1 items-center bg-rose-700 py-2 font-semibold shadow-md shadow-slate-500 px-6 rounded-md text-white"
          >
            <button>Analytics</button>
            <MdAnalytics className="text-md" />
          </div>
        </div>
      </div>

      {/* Analytics Section */}
      <div className={`${analyticToggle ? "flex" : "hidden"} max-h-96 sm:mt-0 mt-5 min-h-96 relative border-t-2 w-full overflow-hidden`}>
        {loader ? (
          <div className="min-h-[calc(450px-140px)] flex justify-center items-center w-full">
            <div className="flex flex-col items-center gap-1">
              <Hourglass
                visible={true}
                height="50"
                width="50"
                ariaLabel="hourglass-loading"
                colors={['#306cce', '#72a1ed']}
              />
              <p className="text-slate-700">Please Wait...</p>
            </div>
          </div>
        ) : (
          <>
            {analyticsData.length === 0 ? (
              <div className="absolute flex flex-col justify-center sm:items-center items-end w-full left-0 top-0 bottom-0 right-0 m-auto">
                <h1 className="text-slate-800 font-serif sm:text-2xl text-[15px] font-bold mb-1">
                  No Data For This Time Period
                </h1>
                <h3 className="sm:w-96 w-[90%] sm:ml-0 pl-6 text-center sm:text-lg text-[12px] text-slate-600">
                  Share your short link to view where your engagements are coming from
                </h3>
              </div>
            ) : (<></>)}
              <Graph graphData={analyticsData} />
          </>
        )}
      </div>
    </div>
  );
};

export default ShortenItem;