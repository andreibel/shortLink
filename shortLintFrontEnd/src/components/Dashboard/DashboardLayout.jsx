import React from 'react';
import Graph from "./Graph.jsx";
import {useStoreContext} from "../../contextApi/ContextApi.jsx";
import {useFetchTotalClicks} from "../../hooks/useQuery.js";
import {useNavigate} from "react-router-dom";
import {dummyData} from "../../DummyData/data.js";
import Loader from "../Loader.jsx";
const DashboardLayout = () => {
  const {token} = useStoreContext();
  const navigate = useNavigate();

  const {isLoading: loader, data: totalClicks} = useFetchTotalClicks(token, onError)
  console.log( useFetchTotalClicks(token, onError));
  function onError() {
    navigate("/error");
  }

  return (
    <div className="lg:px-14 sm:px-8 px-4 min-h-[calc(100vh-64px)]">
      {loader ? (
        <Loader />
      ): (
        <div className="lg:w-[90%] w-full mx-auto py-16">
        <div className=" h-96 relative ">
          {totalClicks.length === 0 && (
            <div className="absolute flex flex-col  justify-center sm:items-center items-end  w-full left-0 top-0 bottom-0 right-0 m-auto">
              <h1 className=" text-slate-800 font-serif sm:text-2xl text-[18px] font-bold mb-1">
                No Data For This Time Period
              </h1>
              <h3 className="sm:w-96 w-[90%] sm:ml-0 pl-6 text-center sm:text-lg text-sm text-slate-600 ">
                Share your short link to view where your engagements are
                coming from
              </h3>
            </div>
          )}
          <Graph graphData={totalClicks} />
        </div>
        <div className='py-5 sm:text-end text-center'>
          <button
            className='bg-custom-gradient px-4 py-2 rounded-md text-white'
            >
            Create a New Short URL
          </button>
        </div>
      </div>)}

    </div>)
}
export default DashboardLayout;