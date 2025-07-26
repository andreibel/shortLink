import {useQuery} from "@tanstack/react-query";
import api from "../api/api";

export const useFetchMyShortUrls = (token, onError) => {
  return useQuery({
    queryKey: ["my-shortenurls"], queryFn: async () => {
      return await api.get("/api/urls/myurls", {
        headers: {
          "Content-Type": "application/json", Accept: "application/json", Authorization: "Bearer " + token,
        },
      });
    }, select: (data) => {
      return data.data.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate));
    }, onError, staleTime: 5000,
  });
};




export const useFetchTotalClicks = (token, onError) => {
  const now = new Date();
  const END_DATE = now.toISOString().slice(0, 10);
  const START_DATE = new Date(now.getFullYear() - 1, 0, 1).toISOString().slice(0, 10);
  return useQuery({
    queryKey: ["url-totalclick"],
    queryFn: async () => {
      console.log("Sending request with token:", token);
      const res = await api.get(
        `/api/urls/totalClicks?startDate=${START_DATE}&endDate=${END_DATE}`,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Got response:", res);
      return res;
    },
    select: (data) => {
      if (!data?.data) return [];
      return Object.keys(data.data).map((key) => ({
        clickDate: key,
        count: data.data[key],
      }));
    },
    onError,
    staleTime: 5000,
  });
};