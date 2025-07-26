import React from 'react'
import { ClipLoader } from 'react-spinners'

function Loader() {
  return (
    <div className="flex justify-center items-center w-full h-[450px]">
      <div className="flex flex-col items-center gap-1">
        <ClipLoader
          loading={true}
          size={65}
          color="red"
          aria-label="Loading Spinner"
          speedMultiplier={0.75}
        />
      </div>
    </div>
  )
}

export default Loader