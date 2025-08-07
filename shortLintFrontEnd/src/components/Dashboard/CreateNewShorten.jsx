import React, { useState } from 'react';
import { useStoreContext } from '../../contextApi/ContextApi';
import { useForm } from 'react-hook-form';
import TextField from '../TextField';
import { Tooltip } from '@mui/material';
import { RxCross2 } from 'react-icons/rx';
import api from '../../api/api';
import toast from 'react-hot-toast';

const CreateNewShorten = ({ setOpen, refetch }) => {
  const { token } = useStoreContext();
  const [loading, setLoading] = useState(false);
  const [shortenUrl, setShortenUrl] = useState('');

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      originalUrl: "",
    },
    mode: "onTouched",
  });

  const createShortUrlHandler = async (data) => {
    setLoading(true);
    try {
      const { data: res } = await api.post("/api/urls/shorten", data, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: "Bearer " + token,
        },
      });

      const fullUrl = `${import.meta.env.VITE_REACT_FRONT_END_URL}/s/${res.shortUrl}`;
      setShortenUrl(fullUrl);
      toast.success("Short URL created!");
      await refetch();
      reset();
    } catch (error) {
      toast.error("Create ShortURL Failed: " );
    } finally {
      setLoading(false);
    }
  };

  const handleCopyClick = async () => {
    try {
      await navigator.clipboard.writeText(shortenUrl);
      toast.success("Short URL Copied to Clipboard", {
        position: "bottom-center",
        className: "mb-5",
        duration: 3000,
      });
      setOpen(false);
    } catch (err) {
      toast.error("Failed to copy URL");
    }
  };

  return (
    <div className="flex justify-center items-center bg-white rounded-md">
      <form
        onSubmit={handleSubmit(createShortUrlHandler)}
        className="sm:w-[450px] w-[360px] relative shadow-custom pt-8 pb-5 sm:px-8 px-4 rounded-lg"
      >
        <h1 className="font-montserrat sm:mt-0 mt-3 text-center font-bold sm:text-2xl text-[22px] text-slate-800">
          Create New Shorten Url
        </h1>

        <hr className="mt-2 sm:mb-5 mb-3 text-slate-950" />

        <TextField
          label="Enter URL"
          required
          id="originalUrl"
          placeholder="https://example.com"
          type="url"
          message="Url is required"
          register={register}
          errors={errors}
        />

        <button
          className="bg-customRed font-semibold text-white w-32 bg-custom-gradient py-2 transition-colors rounded-md my-3"
          type="submit"
        >
          {loading ? "Loading..." : "Create"}
        </button>

        {shortenUrl && (
          <div className="mt-4 flex flex-col items-center gap-2">
            <p className="text-sm text-slate-700 break-all text-center">{shortenUrl}</p>
            <button
              className="bg-blue-600 text-white px-4 py-2 rounded"
              onClick={handleCopyClick}
            >
              Copy Link
            </button>
          </div>
        )}

        {!loading && (
          <Tooltip title="Close">
            <button
              type="button"
              disabled={loading}
              onClick={() => setOpen(false)}
              className="absolute right-2 top-2"
            >
              <RxCross2 className="text-slate-800 text-3xl" />
            </button>
          </Tooltip>
        )}
      </form>
    </div>
  );
};

export default CreateNewShorten;