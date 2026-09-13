package work.socialhub.ksaypip.api.response.me

import work.socialhub.ksaypip.entity.AsideWidgets
import work.socialhub.ksaypip.entity.Feed
import work.socialhub.ksaypip.entity.Me
import work.socialhub.ksaypip.entity.PinnedSubjects
import work.socialhub.ksaypip.entity.Profile

typealias MeMeResponse = Me
typealias MePostsResponse = Feed
typealias MeUpdateProfileResponse = Profile
typealias MePinSubjectResponse = PinnedSubjects
typealias MeUnpinSubjectResponse = PinnedSubjects
typealias MeReorderPinnedSubjectsResponse = PinnedSubjects
typealias MeArrangeAsideWidgetsResponse = AsideWidgets
