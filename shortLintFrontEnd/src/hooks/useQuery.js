import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import api from "../api/api";

/**
 * Custom hook to fetch the user's shortened URLs.
 * Uses React Query for data fetching and caching.
 *
 * @param {string} token - JWT token for authentication.
 * @param {function} [onError] - Optional error handler.
 * @returns {object} React Query result object.
 */
export const useFetchMyShortUrls = (token, onError) => {
  return useQuery({
    queryKey: ["my-shortenurls"],
    queryFn: async () => {
      return await api.get("/api/urls/myurls", {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: "Bearer " + token,
        },
      });
    },
    select: (data) => {
      // Sort URLs by creation date (newest first)
      return data.data.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate));
    },
    onError,
    staleTime: 10000,
  });
};

/**
 * Custom hook to delete a shortened URL.
 * Uses React Query's useMutation for mutation and cache invalidation.
 *
 * @param {object} params - Parameters object.
 * @param {string} params.token - JWT token for authentication.
 * @param {function} [params.onSuccess] - Optional success handler.
 * @param {function} [params.onError] - Optional error handler.
 * @returns {object} React Query mutation object.
 */
export const useDeleteUrl = ({ token, onSuccess, onError }) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (shortUrl) => {
      const res = await api.delete(`/api/urls/${shortUrl}`, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      return res.data;
    },
    onSuccess: (data, variables) => {
      // Invalidate the query to refresh the list after deletion
      queryClient.invalidateQueries({ queryKey: ["my-shortenurls"] });
      onSuccess?.(data, variables);
    },
    onError,
  });
};

/**
 * Custom hook to fetch total clicks for URLs over the past year.
 * Uses React Query for data fetching and caching.
 *
 * @param {string} token - JWT token for authentication.
 * @param {function} [onError] - Optional error handler.
 * @returns {object} React Query result object.
 */
export const useFetchTotalClicks = (token, onError) => {
  const now = new Date();
  const END_DATE = now.toISOString().slice(0, 10);
  const START_DATE = new Date(now.getFullYear() - 1, 0, 1).toISOString().slice(0, 10);
  return useQuery({
    queryKey: ["url-totalclick"],
    queryFn: async () => {
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
      return res;
    },
    select: (data) => {
      // Transform data into array of {clickDate, count} objects
      if (!data?.data) return [];
      return Object.keys(data.data).map((key) => ({
        clickDate: key,
        count: data.data[key],
      }));
    },
    onError,
    staleTime: 10000,
  });
};

/**
 * Custom hook to fetch analytics data for a specific shortened URL.
 * Uses React Query for data fetching and caching.
 *
 * @param {object} params - Parameters object.
 * @param {string} params.shortUrl - The short URL identifier.
 * @param {string} params.token - JWT token for authentication.
 * @param {boolean} [params.enabled=true] - Whether the query is enabled.
 * @param {function} [params.onError] - Optional error handler.
 * @returns {object} React Query result object.
 */
export const useFetchAnalyticsData = ({ shortUrl, token, enabled = true, onError }) => {
  const now = new Date();
  const END_DATE = now.toISOString()
  const START_DATE = new Date(now.getFullYear() - 1, 0, 1).toISOString()
  return useQuery({
    queryKey: ["analytics-data", shortUrl],
    enabled: enabled && !!shortUrl,
    queryFn: async () => {
      const { data } = await api.get(
        `/api/urls/analytics/${shortUrl}?startDate=${START_DATE}&endDate=${END_DATE}`,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
      return data;
    },
    onError,
    staleTime: 1000 * 60 * 5,
  });
};