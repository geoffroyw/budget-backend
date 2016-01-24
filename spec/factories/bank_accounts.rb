FactoryGirl.define do
  factory :bank_account do
    name Faker::Lorem.word
    currency_code Faker::Hacker.abbreviation
  end

end
