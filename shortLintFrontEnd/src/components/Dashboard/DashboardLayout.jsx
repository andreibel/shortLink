import React from 'react';
import Graph from "./Graph.jsx";
import {lessDummyData} from "../../DummyData/data.js";

const DashboardLayout = () => {
  return (
    <div className="lg:px-14 sm:px-8 px-4 min-h-[calc(100vh-64px)]">
      <div className="lg:w-[90%] w-full mx-auto py-16">
        <div className=" h-96 relative ">
          <Graph graphData={lessDummyData}/>
        </div>
      </div>

    </div>)
}
export default DashboardLayout;