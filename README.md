# *Pledge App*

Pledge App is an Android app that aims to reduce the friction associated with non-profit giving. In particular, users must enter their credit card info just once, can easily browse non-profits, and can request matches from their employers as they donate.  The app leverages the [Network for Good API](http://docs.networkforgoodapi.apiary.io/#).

## User Stories

**Required**:

* [ ] Users can login using the Google Oath flow
* [ ] Users can search for contacts using a search bar in the action bar
* [ ] Users can click on any non profit in a list view to view a detail view of the non-profit
  * [ ] Detail view includes mission statemnt, address, website link, link to GuideStar page
* [ ] User can enter credit card info for account using [card.io]() card reader - this info gets stored 
* [ ] User can delete a card on file
  * [ ] Also provide fallback form for manual credit card entry
* [ ] User can donate to a non-profit via a button in the non-profit detail view
* [ ] Post-donation, user receives a receipt
* [ ] User can view her donation history
* [ ] User can browse non-profits by category
* [ ] User can search within a category (via search icon in the action bar)
* [ ] User can browse a list of "Featured" non-profits

**Optional**:

* [ ] User has the option to resend a receipt from the history view
* [ ] Search view shows recent searches
* [ ] Employers can register with Pledge via a basic website
  * [ ] Employer will receive a employer ID code upon registration
  * [ ] Employer can set max amount that the organization will match in a year
* [ ] User can link his/her account to an employer using the registration code
* [ ] User can unlink his/her account from employer
* [ ] Once linked to an employer, user can check a box in donation form to request a match (until he/she has reached her match max amount)
* [ ] Employer can view and approve list of pending matches.
* [ ] Employer can view list of linked employees and unlink any employees
* [ ] Matches get automatically approved for organizations the employer has approved in the past.
* [ ] Push notifications
  * [ ] When an employer approves a match request
  * [ ] To suggest timely non-profits
  * [ ] Periodically, to remind people that they haven't given away any money recently?

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip) - An interactive indicator to navigate between the different pages of a ViewPager

## License

    Copyright 2015 Nikhil Bhargava and Anna Geiduschek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
