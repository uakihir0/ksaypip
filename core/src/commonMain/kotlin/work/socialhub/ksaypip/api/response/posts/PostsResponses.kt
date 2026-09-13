package work.socialhub.ksaypip.api.response.posts

import work.socialhub.ksaypip.entity.Conversation
import work.socialhub.ksaypip.entity.ConversationList
import work.socialhub.ksaypip.entity.Post
import work.socialhub.ksaypip.entity.PostReactions
import work.socialhub.ksaypip.entity.PostReactors

typealias PostsPostResponse = Post
typealias PostsReactionsResponse = PostReactors
typealias PostsConversationsResponse = ConversationList
typealias PostsCreateResponse = Post
typealias PostsStartConversationResponse = Conversation
typealias PostsReactResponse = PostReactions
typealias PostsUnreactResponse = PostReactions
