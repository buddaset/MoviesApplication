package com.example.movies.data.movies.paging


//class MoviePageSource(
//    private val loader: MoviePageLoader,
//) : PagingSource<Int, MovieData>() {
//
//
//    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val page = state.closestPageToPosition(anchorPosition) ?: return null
//        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
//
//
//
//        val pageIndex: Int = params.key ?: 1  // todo check int = 0
//        Log.d("page", " $pageIndex")
//        val pageSize = params.loadSize.coerceAtMost(MovieService.MAX_PAGE_SIZE)
//        return try {
//            val movies = loader(pageIndex, pageSize)
//            val nextKey = if (movies.size < pageSize) null else pageIndex + 1
//            val prevKey = if (pageIndex == 1) null else pageIndex - 1
//            LoadResult.Page(movies, prevKey, nextKey)
//
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//
//
//    }
//}