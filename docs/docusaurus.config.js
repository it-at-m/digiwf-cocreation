/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
    title: 'Miragon BPM Repository Docs',
    tagline: 'Miragon BPM Repository Docs',
    url: 'https://camunda-university-meetup.github.io',
    baseUrl: '/',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/favicon.png',
    organizationName: 'camunda-university-meetup', // Usually your GitHub org/user name.
    projectName: 'exercises', // Usually your repo name.
    themeConfig: {
        navbar: {
            title: 'BPM Repository Docs',
            logo: {
                alt: 'BPM Repository Logo',
                src: 'img/logo.png',
            },
            items: [
                {
                    to: "/",
                    activeBasePath: "manual",
                    label: "Manual",
                    position: "left",
                },
                {
                    to: "api/0",
                    activeBasePath: "api",
                    label: "API",
                    position: "left",
                },
                {
                    href: 'https://github.com/camunda-university-meetup/exercises',
                    label: 'GitHub',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: 'dark',
            links: [
                {
                    title: 'Docs',
                    items: [
                        {
                            label: 'Instructions',
                            to: '/instructions/deploying-artifacts',
                        },
                    ],
                },
                {
                    title: 'Community',
                    items: [
                        {
                            label: 'Camunda Forum',
                            href: 'https://forum.camunda.org/',
                        },
                        {
                            label: 'Camunda University Meetup',
                            href: 'https://www.meetup.com/de-DE/camunda-university-group/',
                        }
                    ],
                },
                {
                    title: 'More',
                    items: [
                        {
                            label: 'GitHub',
                            href: 'https://github.com/camunda-community-hub',
                        },
                    ],
                },
            ],
            copyright: `Copyright © ${new Date().getFullYear()} FlowSquad GmbH`,
        },
    },
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    routeBasePath: '/'
                },
                blog: {
                    showReadingTime: true,
                    // Please change this to your repo.
                    editUrl:
                        'https://github.com/facebook/docusaurus/edit/master/website/blog/',
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
            },
        ],
        [
            'redocusaurus',
            {
                specs: [{
                    spec: './static/assets/openapi.json',
                }],
            }
        ],
    ],
};
