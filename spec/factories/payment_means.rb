FactoryGirl.define do
  factory :payment_mean do
    name Faker::Lorem.word
    currency_code Faker::Hacker.abbreviation
  end

end
